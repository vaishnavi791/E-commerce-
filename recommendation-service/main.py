from collections import defaultdict
from typing import Literal

from fastapi import FastAPI
from pydantic import BaseModel, Field
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

app = FastAPI(title="Mira Recommendation Service", version="1.0.0")

InteractionType = Literal["VIEW", "CART_ADD", "PURCHASE"]


class RecommendationProduct(BaseModel):
    id: int
    category: str | None = None
    subcategory: str | None = None
    brand: str | None = None
    description: str | None = None


class RecommendationInteraction(BaseModel):
    productId: int
    interactionType: InteractionType


class RecommendationRequest(BaseModel):
    products: list[RecommendationProduct]
    interactions: list[RecommendationInteraction] = Field(default_factory=list)
    limit: int = Field(default=4, ge=1, le=20)


class RecommendationResponse(BaseModel):
    recommendedProductIds: list[int]


INTERACTION_WEIGHTS = {
    "VIEW": 1.0,
    "CART_ADD": 2.0,
    "PURCHASE": 3.0,
}

SUBCATEGORY_ALIASES = {
    "jewelry": "jewellery",
}


def normalize_taxonomy(value: str | None, aliases: dict[str, str] | None = None) -> str:
    normalized = (value or "").strip().casefold()
    return (aliases or {}).get(normalized, normalized)


def normalized_subcategory(product: RecommendationProduct) -> str:
    return normalize_taxonomy(product.subcategory, SUBCATEGORY_ALIASES)


def normalized_category(product: RecommendationProduct) -> str:
    return normalize_taxonomy(product.category)


def feature_text(product: RecommendationProduct) -> str:
    return " ".join(
        value or ""
        for value in (
            product.brand,
            product.description,
        )
    ).strip()


def recent_product_ids(products: list[RecommendationProduct], limit: int) -> list[int]:
    return [product.id for product in reversed(products[-limit:])]


def add_ranked_candidates(
    recommended_ids: list[int],
    candidate_indexes: list[int],
    products: list[RecommendationProduct],
    scores,
    interacted_ids: set[int],
    limit: int,
) -> None:
    for index in sorted(candidate_indexes, key=lambda candidate: scores[candidate], reverse=True):
        product_id = products[index].id
        if product_id not in interacted_ids and product_id not in recommended_ids:
            recommended_ids.append(product_id)
        if len(recommended_ids) == limit:
            return


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok"}


@app.post("/recommend", response_model=RecommendationResponse)
def recommend(request: RecommendationRequest) -> RecommendationResponse:
    if not request.products:
        return RecommendationResponse(recommendedProductIds=[])

    if not request.interactions:
        return RecommendationResponse(
            recommendedProductIds=recent_product_ids(request.products, request.limit)
        )

    vectorizer = TfidfVectorizer(stop_words="english")
    product_matrix = vectorizer.fit_transform(
        [feature_text(product) for product in request.products]
    )
    product_index = {product.id: index for index, product in enumerate(request.products)}

    profile = None
    interacted_ids: set[int] = set()
    subcategory_affinity: defaultdict[str, float] = defaultdict(float)
    subcategory_category_affinity: defaultdict[tuple[str, str], float] = defaultdict(float)
    for interaction in request.interactions:
        product_index_value = product_index.get(interaction.productId)
        if product_index_value is None:
            continue
        weight = INTERACTION_WEIGHTS[interaction.interactionType]
        interacted_product = request.products[product_index_value]
        current_vector = product_matrix[product_index_value] * weight
        profile = current_vector if profile is None else profile + current_vector
        interacted_ids.add(interaction.productId)

        subcategory = normalized_subcategory(interacted_product)
        category = normalized_category(interacted_product)
        if subcategory:
            subcategory_affinity[subcategory] += weight
            subcategory_category_affinity[(subcategory, category)] += weight

    if profile is None:
        return RecommendationResponse(
            recommendedProductIds=recent_product_ids(request.products, request.limit)
        )

    scores = cosine_similarity(profile, product_matrix).ravel()
    recommended_ids: list[int] = []
    preferred_subcategories = sorted(
        subcategory_affinity,
        key=lambda subcategory: subcategory_affinity[subcategory],
        reverse=True,
    )

    if preferred_subcategories:
        primary_subcategory = preferred_subcategories[0]
        primary_category = max(
            (
                category
                for subcategory, category in subcategory_category_affinity
                if subcategory == primary_subcategory
            ),
            key=lambda category: subcategory_category_affinity[
                (primary_subcategory, category)
            ],
        )

        # Tier 1: products from the strongest subcategory always rank first.
        add_ranked_candidates(
            recommended_ids,
            [
                index
                for index, product in enumerate(request.products)
                if normalized_subcategory(product) == primary_subcategory
            ],
            request.products,
            scores,
            interacted_ids,
            request.limit,
        )

        # Tier 2: additional strong preferences in the same broader category,
        # followed by the remaining products in that category.
        for subcategory in preferred_subcategories[1:]:
            add_ranked_candidates(
                recommended_ids,
                [
                    index
                    for index, product in enumerate(request.products)
                    if normalized_category(product) == primary_category
                    and normalized_subcategory(product) == subcategory
                ],
                request.products,
                scores,
                interacted_ids,
                request.limit,
            )
        add_ranked_candidates(
            recommended_ids,
            [
                index
                for index, product in enumerate(request.products)
                if normalized_category(product) == primary_category
            ],
            request.products,
            scores,
            interacted_ids,
            request.limit,
        )

        # Tier 3: other meaningful subcategory preferences, then all remaining products.
        for subcategory in preferred_subcategories[1:]:
            add_ranked_candidates(
                recommended_ids,
                [
                    index
                    for index, product in enumerate(request.products)
                    if normalized_subcategory(product) == subcategory
                ],
                request.products,
                scores,
                interacted_ids,
                request.limit,
            )
        add_ranked_candidates(
            recommended_ids,
            list(range(len(request.products))),
            request.products,
            scores,
            interacted_ids,
            request.limit,
        )
    else:
        add_ranked_candidates(
            recommended_ids,
            list(range(len(request.products))),
            request.products,
            scores,
            interacted_ids,
            request.limit,
        )

    if len(recommended_ids) < request.limit:
        for product_id in recent_product_ids(request.products, request.limit):
            if product_id not in recommended_ids:
                recommended_ids.append(product_id)
            if len(recommended_ids) == request.limit:
                break

    return RecommendationResponse(recommendedProductIds=recommended_ids)
