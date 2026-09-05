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


def feature_text(product: RecommendationProduct) -> str:
    return " ".join(
        value or ""
        for value in (
            product.category,
            product.subcategory,
            product.brand,
            product.description,
        )
    ).strip()


def recent_product_ids(products: list[RecommendationProduct], limit: int) -> list[int]:
    return [product.id for product in reversed(products[-limit:])]


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
    for interaction in request.interactions:
        product_index_value = product_index.get(interaction.productId)
        if product_index_value is None:
            continue
        weight = INTERACTION_WEIGHTS[interaction.interactionType]
        current_vector = product_matrix[product_index_value] * weight
        profile = current_vector if profile is None else profile + current_vector
        interacted_ids.add(interaction.productId)

    if profile is None:
        return RecommendationResponse(
            recommendedProductIds=recent_product_ids(request.products, request.limit)
        )

    scores = cosine_similarity(profile, product_matrix).ravel()
    ranked_indexes = sorted(
        range(len(request.products)),
        key=lambda index: scores[index],
        reverse=True,
    )
    recommended_ids = [
        request.products[index].id
        for index in ranked_indexes
        if request.products[index].id not in interacted_ids
    ][: request.limit]

    if len(recommended_ids) < request.limit:
        for product_id in recent_product_ids(request.products, request.limit):
            if product_id not in recommended_ids:
                recommended_ids.append(product_id)
            if len(recommended_ids) == request.limit:
                break

    return RecommendationResponse(recommendedProductIds=recommended_ids)
