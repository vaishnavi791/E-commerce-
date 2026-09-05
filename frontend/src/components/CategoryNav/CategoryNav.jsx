import { Link } from "react-router-dom";

const categoryGroups = {
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

export default function CategoryNav() {
  return (
    <div className="category-nav">
      {Object.entries(categoryGroups).map(([category, items]) => (
        <div key={category}>
          <Link to={`/products?category=${category}`}>{category}</Link>
          <span>{items.join(" · ")}</span>
        </div>
      ))}
    </div>
  );
}
