import { createContext, useContext, useState } from "react";

const WishlistContext = createContext(null);

export function WishlistProvider({ children }) {
  const [wishlistIds, setWishlistIds] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("mira_wishlist") || "[]");
    } catch {
      return [];
    }
  });

  function toggleWishlist(productId) {
    setWishlistIds((currentIds) => {
      const nextIds = currentIds.includes(productId)
        ? currentIds.filter((id) => id !== productId)
        : [...currentIds, productId];

      localStorage.setItem("mira_wishlist", JSON.stringify(nextIds));
      return nextIds;
    });
  }

  return (
    <WishlistContext.Provider value={{ wishlistIds, toggleWishlist }}>
      {children}
    </WishlistContext.Provider>
  );
}

export function useWishlist() {
  return useContext(WishlistContext);
}