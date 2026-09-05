const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

export const endpoints = {
  register: "/auth/register",
  login: "/auth/login",
  products: "/products",
  product: (id) => `/products/${id}`,
  cart: "/cart",
  cartItems: "/cart/items",
  cartItem: (productId) => `/cart/items/${productId}`,
  checkout: "/orders/checkout",
  orders: "/orders",
  recommendations: "/api/recommendations",
  cancelOrder: (orderId) => `/orders/${orderId}/cancel`,
};

export async function apiFetch(path, options = {}) {
  const token = localStorage.getItem("mira_token");
  const headers = new Headers(options.headers);

  if (options.body && !headers.has("Content-Type")) {
    headers.set("Content-Type", "application/json");
  }

  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  let response;
  try {
    response = await fetch(`${API_BASE_URL}${path}`, { ...options, headers });
  } catch {
    throw new Error(
      "Unable to reach the store. Check that the backend is running.",
    );
  }
  if (!response.ok) {
    let message = `Request failed with status ${response.status}`;
    try {
      const error = await response.json();
      message = error.message || message;
    } catch {
      // Some backend errors have no JSON body.
    }
    throw new Error(message);
  }

  return response.status === 204 ? null : response.json();
}
