import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import ErrorMessage from "../../components/ErrorMessage/ErrorMessage";
import Loading from "../../components/Loading/Loading";
import ProductGrid from "../../components/ProductGrid/ProductGrid";
import { useAuth } from "../../context/AuthContext";
import { useWishlist } from "../../context/WishlistContext";
import { apiFetch, endpoints } from "../../services/api";

export default function Wishlist() {
  const { token } = useAuth();
  const { wishlistIds } = useWishlist();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!token || !wishlistIds.length) {
      setProducts([]);
      setLoading(false);
      return;
    }

    setLoading(true);
    Promise.all(wishlistIds.map((id) => apiFetch(endpoints.product(id))))
      .then(setProducts)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, wishlistIds]);

  return (
    <section className="content-page">
      <div className="page-heading">
        <p className="eyebrow">Saved for later</p>
        <h1>Wishlist</h1>
        <p>
          {wishlistIds.length} saved product{wishlistIds.length === 1 ? "" : "s"}
        </p>
      </div>
      {!token ? (
        <div className="empty-state">
          <h2>Sign in to view your wishlist</h2>
          <Link className="button" to="/login">
            Sign in
          </Link>
        </div>
      ) : loading ? (
        <Loading />
      ) : error ? (
        <ErrorMessage message={error} />
      ) : products.length ? (
        <ProductGrid products={products} />
      ) : (
        <div className="empty-state">
          <h2>Your wishlist is empty</h2>
          <Link className="button" to="/products">
            Explore products
          </Link>
        </div>
      )}
    </section>
  );
}