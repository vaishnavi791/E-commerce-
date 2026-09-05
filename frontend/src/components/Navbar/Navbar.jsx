import { Link, NavLink } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useCart } from "../../context/CartContext";
import SearchBar from "../SearchBar/SearchBar";

const categories = ["Clothing", "Footwear", "Accessories", "Beauty"];

export default function Navbar() {
  const { token, signOut } = useAuth();
  const { cart } = useCart();
  const bagCount = cart?.productIds?.length || 0;

  return (
    <header className="site-header">
      <div className="announcement">Thoughtful style for every day</div>
      <div className="nav-wrap">
        <Link className="brand" to="/">
          mira<span>.</span>
        </Link>
        <nav className="primary-nav" aria-label="Main navigation">
          <NavLink to="/products">New in</NavLink>
          {categories.map((category) => (
            <NavLink key={category} to={`/products?category=${category}`}>
              {category}
            </NavLink>
          ))}
        </nav>
        <div className="nav-actions">
          <SearchBar />
          <NavLink
            className="nav-icon"
            to={token ? "/orders" : "/login"}
            aria-label="Account"
          >
            Profile
          </NavLink>
          <NavLink className="nav-icon" to="/wishlist" aria-label="Wishlist">
            Wishlist
          </NavLink>
          {token && (
            <button className="link-button" type="button" onClick={signOut}>
              Sign out
            </button>
          )}
          <NavLink className="nav-icon" to="/cart" aria-label="Shopping bag">
            Bag ({bagCount})
          </NavLink>
        </div>
      </div>
    </header>
  );
}
