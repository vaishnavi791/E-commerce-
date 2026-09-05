import { useEffect, useMemo, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import ProductGrid from "../../components/ProductGrid/ProductGrid";
import Loading from "../../components/Loading/Loading";
import ErrorMessage from "../../components/ErrorMessage/ErrorMessage";
import { apiFetch, endpoints } from "../../services/api";
import { useAuth } from "../../context/AuthContext";

export default function Products() {
  const { token } = useAuth();
  const [params, setParams] = useSearchParams();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [filters, setFilters] = useState({
    search: params.get("search") || "",
    category: params.get("category") || "",
    subcategory: "",
    brand: "",
    maxPrice: "",
    sort: "featured",
  });
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
  const subcategories = {
    Clothing: [
      "Dresses",
      "Tops",
      "Shirts",
      "Jeans",
      "Trousers",
      "Skirts",
      "Ethnic Wear",
    ],
    Footwear: ["Heels", "Flats", "Sneakers", "Sandals", "Boots"],
    Accessories: ["Handbags", "Jewellery", "Watches", "Sunglasses", "Belts"],
    Beauty: ["Makeup", "Skincare", "Haircare", "Fragrance"],
  };
  const filtered = useMemo(
    () =>
      products
        .filter(
          (product) =>
            (!filters.search ||
              product.pName
                .toLowerCase()
                .includes(filters.search.toLowerCase())) &&
            (!filters.category ||
              product.category.toLowerCase() ===
                filters.category.toLowerCase()) &&
            (!filters.subcategory ||
              product.subcategory.toLowerCase() ===
                filters.subcategory.toLowerCase()) &&
            (!filters.brand ||
              product.brand.toLowerCase() === filters.brand.toLowerCase()) &&
            (!filters.maxPrice || product.price <= Number(filters.maxPrice)),
        )
        .sort((a, b) =>
          filters.sort === "low"
            ? a.price - b.price
            : filters.sort === "high"
              ? b.price - a.price
              : 0,
        ),
    [products, filters],
  );
  function updateFilter(key, value) {
    const next = { ...filters, [key]: value };
    setFilters(next);
    if (key === "search" || key === "category")
      setParams(value ? { [key]: value } : {});
  }
  if (!token) {
    return (
      <section className="content-page">
        <div className="empty-state">
          <h1>Sign in to browse products</h1>
          <Link className="button" to="/login">
            Sign in
          </Link>
        </div>
      </section>
    );
  }

  return (
    <section className="content-page">
      <div className="page-heading">
        <p className="eyebrow">The collection</p>
        <h1>All products</h1>
        <p>
          {products.length
            ? `${filtered.length} pieces available`
            : "Clothing, footwear, accessories and beauty for her."}
        </p>
      </div>
      <div className="filters">
        <input
          value={filters.search}
          onChange={(event) => updateFilter("search", event.target.value)}
          placeholder="Search products"
          aria-label="Search products"
        />
        <select
          value={filters.category}
          onChange={(event) =>
            setFilters({
              ...filters,
              category: event.target.value,
              subcategory: "",
            })
          }
        >
          <option value="">All categories</option>
          {Object.keys(subcategories).map((item) => (
            <option key={item}>{item}</option>
          ))}
        </select>
        <select
          aria-label="Subcategory filter"
          value={filters.subcategory}
          onChange={(event) => updateFilter("subcategory", event.target.value)}
          disabled={!filters.category}
        >
          <option value="">All subcategories</option>
          {(subcategories[filters.category] || []).map((item) => (
            <option key={item}>{item}</option>
          ))}
        </select>
        <select
          aria-label="Brand filter"
          value={filters.brand}
          onChange={(event) => updateFilter("brand", event.target.value)}
        >
          <option value="">All brands</option>
          {[...new Set(products.map((product) => product.brand))].map(
            (brand) => (
              <option key={brand}>{brand}</option>
            ),
          )}
        </select>
        <select
          value={filters.maxPrice}
          onChange={(event) => updateFilter("maxPrice", event.target.value)}
        >
          <option value="">Any price</option>
          <option value="3000">Under ₹3,000</option>
          <option value="6000">Under ₹6,000</option>
          <option value="10000">Under ₹10,000</option>
        </select>
        <select
          value={filters.sort}
          onChange={(event) => updateFilter("sort", event.target.value)}
        >
          <option value="featured">Sort: Featured</option>
          <option value="low">Price: low to high</option>
          <option value="high">Price: high to low</option>
        </select>
      </div>
      {loading ? (
        <Loading />
      ) : error ? (
        <ErrorMessage message={error} />
      ) : (
        <ProductGrid products={filtered} />
      )}
    </section>
  );
}
