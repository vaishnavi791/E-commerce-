import { Link } from "react-router-dom";

export default function ProductCard({ product }) {
  return (
    <article className="product-card">
      <div className="product-image">
        {product?.imageUrl ? (
          <img
            src={product.imageUrl}
            alt={product.pName || "Product"}
            onError={(event) => {
              event.currentTarget.style.display = "none";
              event.currentTarget.parentElement.classList.add("image-fallback");
            }}
          />
        ) : (
          "Product image unavailable"
        )}
      </div>
      <div className="product-card-info">
        <div>
          <p className="eyebrow">
            {product?.brand || product?.category || "Collection"}
          </p>
          <h3>{product?.pName || "Product name"}</h3>
          {product?.subcategory && <span>{product.subcategory}</span>}
        </div>
        <p>
          ₹{product?.price == null ? "0.00" : Number(product.price).toFixed(2)}
        </p>
      </div>
      {product?.id && (
        <Link className="stretched-link" to={`/products/${product.id}`}>
          View product
        </Link>
      )}
    </article>
  );
}
