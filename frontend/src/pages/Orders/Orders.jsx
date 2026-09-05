import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { apiFetch, endpoints } from "../../services/api";
import Loading from "../../components/Loading/Loading";
import ErrorMessage from "../../components/ErrorMessage/ErrorMessage";

export default function Orders() {
  const { token } = useAuth();
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    if (!token) {
      setLoading(false);
      return;
    }
    apiFetch(endpoints.orders)
      .then(setOrders)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token]);
  if (!token)
    return (
      <section className="content-page">
        <div className="empty-state">
          <h1>Sign in to view orders</h1>
          <Link className="button" to="/login">
            Sign in
          </Link>
        </div>
      </section>
    );
  return (
    <section className="content-page">
      <div className="page-heading">
        <p className="eyebrow">Your account</p>
        <h1>Orders</h1>
        <p>Your order history, from the backend.</p>
      </div>
      {loading ? (
        <Loading />
      ) : error ? (
        <ErrorMessage message={error} />
      ) : orders.length ? (
        <div className="order-list">
          {orders.map((order) => (
            <article className="order-item" key={order.id}>
              <div>
                <p className="eyebrow">Order #{order.id}</p>
                <h2>{order.status}</h2>
                <p>
                  {order.orderDate
                    ? new Date(order.orderDate).toLocaleDateString()
                    : "Date unavailable"}{" "}
                  · {order.productIds?.length || 0} products
                </p>
              </div>
              <strong>₹{Number(order.totalAmount || 0).toFixed(2)}</strong>
            </article>
          ))}
        </div>
      ) : (
        <div className="empty-state">
          <h2>No orders yet</h2>
          <Link className="button" to="/products">
            Shop products
          </Link>
        </div>
      )}
    </section>
  );
}
