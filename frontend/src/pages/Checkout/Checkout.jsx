import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useCart } from "../../context/CartContext";
import { apiFetch, endpoints } from "../../services/api";

export default function Checkout() {
  const { token } = useAuth();
  const { setCart } = useCart();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);
  async function submit() {
    setBusy(true);
    try {
      await apiFetch(endpoints.checkout, { method: "POST" });
      setCart({ cartId: null, productIds: [], productNames: [] });
      navigate("/orders");
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  }
  if (!token)
    return (
      <section className="content-page">
        <div className="empty-state">
          <h1>Sign in to checkout</h1>
          <Link className="button" to="/login">
            Sign in
          </Link>
        </div>
      </section>
    );
  return (
    <section className="auth-page">
      <div className="form-intro">
        <p className="eyebrow">Almost yours</p>
        <h1>Place your order</h1>
        {error && <p className="form-error">{error}</p>}
        <button className="button" onClick={submit} disabled={busy}>
          {busy ? "Placing order..." : "Place order"}
        </button>
      </div>
    </section>
  );
}
