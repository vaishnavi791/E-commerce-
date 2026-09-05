import ProductCard from "../ProductCard/ProductCard";

export default function ProductGrid({ products = [] }) {
  return (
    <div className="product-grid">
      {products.length ? (
        products.map((product) => (
          <ProductCard key={product.id || product.pName} product={product} />
        ))
      ) : (
        <p className="empty-inline">No products found.</p>
      )}
    </div>
  );
}
