import { createContext, useContext, useState } from "react";
import { apiFetch, endpoints } from "../services/api";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem("mira_token"));

  function signIn(nextToken, email = "") {
    localStorage.setItem("mira_token", nextToken);
    localStorage.setItem("mira_email", email);
    setToken(nextToken);
  }

  async function login(credentials) {
    const result = await apiFetch(endpoints.login, {
      method: "POST",
      body: JSON.stringify(credentials),
    });
    signIn(result.token, result.email);
    return result;
  }

  async function register(details) {
    const result = await apiFetch(endpoints.register, {
      method: "POST",
      body: JSON.stringify(details),
    });
    signIn(result.token, result.email);
    return result;
  }

  function signOut() {
    localStorage.removeItem("mira_token");
    localStorage.removeItem("mira_email");
    setToken(null);
  }

  return (
    <AuthContext.Provider
      value={{
        token,
        email: localStorage.getItem("mira_email"),
        login,
        register,
        signOut,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
