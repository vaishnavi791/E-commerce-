import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import CategoryNav from "../../components/CategoryNav/CategoryNav";
import ProductGrid from "../../components/ProductGrid/ProductGrid";
import Loading from "../../components/Loading/Loading";
import ErrorMessage from "../../components/ErrorMessage/ErrorMessage";
import { apiFetch, endpoints } from "../../services/api";
import { useAuth } from "../../context/AuthContext";

export default function Home() {
  const { token } = useAuth();
  const [products, setProducts] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    if (!token) {
      setLoading(false);
      return;
    }
    setLoading(true);
    apiFetch(endpoints.products)
      .then(setProducts)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token]);
  return (
    <>
      <section className="hero">
        <div>
          <p className="eyebrow">The women&apos;s edit / 2026</p>
          <h1>
            Wear what
            <br />
            feels like you.
          </h1>
          <p className="lede">
            Thoughtful clothing, accessories and beauty for the everyday.
          </p>
          <Link className="button" to="/products">
            Explore the collection
          </Link>
        </div>
        <div className="hero-art">
          <span>
            New season
            <br />
            arrivals
          </span>
        </div>
      </section>
      <section className="home-section">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Browse the edit</p>
            <h2>Shop by category</h2>
          </div>
          <Link to="/products">View all</Link>
        </div>
        <CategoryNav />
      </section>
      <section className="home-section">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Most wanted</p>
            <h2>Trending products</h2>
          </div>
        </div>
        {loading ? (
          <Loading />
        ) : error ? (
          <ErrorMessage message={error} />
        ) : products.length ? (
          <ProductGrid products={products.slice(0, 4)} />
        ) : (
          <div className="empty-inline">No products are available yet.</div>
        )}
      </section>
      <section className="home-section">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Just landed</p>
            <h2>New arrivals</h2>
          </div>
        </div>
        {loading ? (
          <Loading />
        ) : error ? (
          <ErrorMessage message={error} />
        ) : products.length ? (
          <ProductGrid products={products.slice(0, 4)} />
        ) : (
          <div className="empty-inline">No new arrivals are available yet.</div>
        )}
      </section>
      <section className="recommendation">
        <p className="eyebrow">A little something for you</p>
        <h2>Recommended for you</h2>
        <p>
          Personal recommendations will appear as your collection takes shape.
        </p>
        <Link to="/products">Start exploring</Link>
      </section>
    </>
  );
}
