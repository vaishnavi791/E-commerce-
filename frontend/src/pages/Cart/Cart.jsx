import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useCart } from "../../context/CartContext";
import { apiFetch, endpoints } from "../../services/api";
import Loading from "../../components/Loading/Loading";
import ErrorMessage from "../../components/ErrorMessage/ErrorMessage";

export default function Cart() {
  const { token } = useAuth();
  const { cart, refreshCart, updateQuantity, removeItem } = useCart();
  const [products, setProducts] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [mutating, setMutating] = useState(false);
  useEffect(() => {
    let active = true;
    if (!token) {
      setLoading(false);
      return undefined;
    }
    setLoading(true);
    setError("");
    refreshCart()
      .then((result) =>
        Promise.all(
          [...new Set(result.productIds || [])].map(async (id) => ({
            ...(await apiFetch(endpoints.product(id))),
            id,
          })),
        ),
      )
      .then((items) => {
        if (active) setProducts(items);
      })
      .catch((err) => {
        if (active) setError(err.message);
      })
      .finally(() => {
        if (active) setLoading(false);
      });
    return () => {
      active = false;
    };
  }, [token, refreshCart]);
  const quantities = (cart?.productIds || []).reduce(
    (result, id) => ({ ...result, [id]: (result[id] || 0) + 1 }),
    {},
  );
  const subtotal = Number(cart?.subtotal || 0);
  async function mutate(action) {
    setMutating(true);
    setError("");
    try {
      await action();
      const updated = await refreshCart();
      const items = await Promise.all(
        [...new Set(updated.productIds || [])].map(async (id) => ({
          ...(await apiFetch(endpoints.product(id))),
          id,
        })),
      );
      setProducts(items);
    } catch (err) {
      setError(err.message);
    } finally {
      setMutating(false);
    }
  }
  if (!token)
    return (
      <section className="content-page">
        <div className="empty-state">
          <p className="eyebrow">Your bag</p>
          <h1>Sign in to see your bag</h1>
          <Link className="button" to="/login">
            Sign in
          </Link>
        </div>
      </section>
    );
  if (loading)
    return (
      <section className="content-page">
        <Loading />
      </section>
    );
  if (error)
    return (
      <section className="content-page">
        <ErrorMessage message={error} />
      </section>
    );
  return (
    <section className="content-page">
      <div className="page-heading">
        <p className="eyebrow">Your bag</p>
        <h1>Shopping bag</h1>
        <p>
          {cart?.productIds?.length || 0} item
          {(cart?.productIds?.length || 0) === 1 ? "" : "s"}
        </p>
      </div>
      {error && <ErrorMessage message={error} />}
      {products.length ? (
        <div className="cart-layout">
          <div className="cart-list">
            {products.map((product) => (
              <article className="cart-item" key={product.id}>
                <div className="cart-thumb">
                  {product.imageUrl ? (
                    <img
                      src={product.imageUrl}
                      alt={product.pName}
                      onError={(event) => {
                        event.currentTarget.style.display = "none";
                        event.currentTarget.parentElement.classList.add(
                          "image-fallback",
                        );
                      }}
                    />
                  ) : (
                    "Product image unavailable"
                  )}
                </div>
                <div>
                  <p className="eyebrow">{product.category}</p>
                  <h3>{product.pName}</h3>
                  <p>₹{Number(product.price).toFixed(2)}</p>
                  <p className="muted">
                    {quantities[product.id] || 0} × price = ₹
                    {(Number(product.price) * (quantities[product.id] || 0)).toFixed(2)}
                  </p>
                  <div className="quantity">
                    <button
                      disabled={mutating}
                      onClick={() =>
                        mutate(() =>
                          updateQuantity(
                            product.id,
                            Math.max(0, quantities[product.id] - 1),
                          ),
                        )
                      }
                    >
                      −
                    </button>
                    <span>{quantities[product.id] || 0}</span>
                    <button
                      disabled={mutating}
                      onClick={() =>
                        mutate(() =>
                          updateQuantity(
                            product.id,
                            (quantities[product.id] || 0) + 1,
                          ),
                        )
                      }
                    >
                      +
                    </button>
                    <button
                      className="remove"
                      disabled={mutating}
                      onClick={() => mutate(() => removeItem(product.id))}
                    >
                      Remove
                    </button>
                  </div>
                </div>
              </article>
            ))}
          </div>
          <aside className="summary">
            <h2>Summary</h2>
            <p>Subtotal <strong>₹{subtotal.toFixed(2)}</strong></p>
            <p>Total <strong>₹{subtotal.toFixed(2)}</strong></p>
            <p>The backend confirms the final total from current product prices at checkout.</p>
            <Link className="button" to="/checkout">
              Checkout
            </Link>
          </aside>
        </div>
      ) : (
        !loading && (
          <div className="empty-state">
            <h2>Your bag is empty</h2>
            <Link className="button" to="/products">
              Shop products
            </Link>
          </div>
        )
      )}
    </section>
  );
}
