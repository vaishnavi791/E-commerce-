import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { apiFetch, endpoints } from "../../services/api";
import { useCart } from "../../context/CartContext";
import Loading from "../../components/Loading/Loading";
import ErrorMessage from "../../components/ErrorMessage/ErrorMessage";

export default function ProductDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { addItem } = useCart();
  const [product, setProduct] = useState(null);
  const [similar, setSimilar] = useState([]);
  const [error, setError] = useState("");
  const [adding, setAdding] = useState(false);
  useEffect(() => {
    let active = true;
    setProduct(null);
    setSimilar([]);
    setError("");
    apiFetch(endpoints.product(id))
      .then((result) => {
        if (active) setProduct(result);
      })
      .catch((err) => {
        if (active) setError(err.message);
      });
    return () => {
      active = false;
    };
  }, [id]);
  useEffect(() => {
    if (!product?.category) return undefined;
    let active = true;
    apiFetch(endpoints.products)
      .then((items) => {
        if (active)
          setSimilar(
            items
              .filter((item) => item.category === product.category)
              .slice(0, 4),
          );
      })
      .catch(() => {});
    return () => {
      active = false;
    };
  }, [product?.category]);
  async function addToCart() {
    setAdding(true);
    try {
      await addItem(id);
      navigate("/cart");
    } catch (err) {
      setError(err.message);
    } finally {
      setAdding(false);
    }
  }
  if (error)
    return (
      <section className="content-page">
        <ErrorMessage message={error} />
      </section>
    );
  if (!product)
    return (
      <section className="content-page">
        <Loading />
      </section>
    );
  return (
    <>
      <section className="details-page">
        <div className="detail-image">Product image unavailable</div>
        <div className="detail-copy">
          <p className="eyebrow">{product.category}</p>
          <h1>{product.pName}</h1>
          <p className="detail-price">£{Number(product.price).toFixed(2)}</p>
          <p className="muted">
            Brand, discount, rating, description and product images are not
            provided by the backend.
          </p>
          <p className="stock">
            {product.quantity > 0
              ? `${product.quantity} in stock`
              : "Out of stock"}
          </p>
          <div className="detail-actions">
            <button
              className="button"
              disabled={!product.quantity || adding}
              onClick={addToCart}
            >
              {adding ? "Adding..." : "Add to bag"}
            </button>
            <button
              className="outline-button"
              disabled={!product.quantity}
              onClick={addToCart}
            >
              Buy now
            </button>
          </div>
          <div className="detail-meta">
            <span>Brand unavailable</span>
            <span>Rating unavailable</span>
            <span>Discount unavailable</span>
          </div>
        </div>
      </section>
      <section className="home-section similar">
        <div className="section-heading">
          <div>
            <p className="eyebrow">You may also like</p>
            <h2>Similar products</h2>
          </div>
        </div>
        <div className="muted">
          {similar.length ? (
            <div className="similar-list">
              {similar.map((item) => (
                <div key={item.pName}>
                  <strong>{item.pName}</strong>
                  <span>£{Number(item.price).toFixed(2)}</span>
                </div>
              ))}
            </div>
          ) : (
            "Similar products will appear when the backend returns matching products."
          )}
        </div>
      </section>
    </>
  );
}
