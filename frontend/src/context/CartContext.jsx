import { createContext, useCallback, useContext, useState } from "react";
import { apiFetch, endpoints } from "../services/api";

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const [cart, setCart] = useState(null);

  const refreshCart = useCallback(async () => {
    const result = await apiFetch(endpoints.cart);
    setCart(result);
    return result;
  }, []);

  const addItem = useCallback(async (productId) => {
    const result = await apiFetch(endpoints.cartItems, {
      method: "POST",
      body: JSON.stringify({ productId }),
    });
    setCart(result);
    return result;
  }, []);

  const updateQuantity = useCallback(async (productId, quantity) => {
    const result = await apiFetch(
      `${endpoints.cartItem(productId)}?quantity=${quantity}`,
      { method: "PUT" },
    );
    setCart(result);
    return result;
  }, []);

  const removeItem = useCallback(async (productId) => {
    const result = await apiFetch(endpoints.cartItem(productId), {
      method: "DELETE",
    });
    setCart(result);
    return result;
  }, []);

  async function clearCart() {
    await apiFetch(endpoints.cart, { method: "DELETE" });
    setCart({ cartId: cart?.cartId, productIds: [], productNames: [] });
  }

  return (
    <CartContext.Provider
      value={{
        cart,
        setCart,
        refreshCart,
        addItem,
        updateQuantity,
        removeItem,
        clearCart,
      }}
    >
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  return useContext(CartContext);
}
