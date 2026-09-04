import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);
  async function submit(event) {
    event.preventDefault();
    setBusy(true);
    setError("");
    try {
      await login(form);
      navigate("/products");
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  }
  return (
    <section className="auth-page">
      <div className="form-intro">
        <p className="eyebrow">Welcome back</p>
        <h1>Sign in</h1>
        <p>Use your mira account to view your bag and orders.</p>
      </div>
      <form className="auth-form" onSubmit={submit}>
        {error && <p className="form-error">{error}</p>}
        <label>
          Email
          <input
            type="email"
            required
            value={form.email}
            onChange={(event) =>
              setForm({ ...form, email: event.target.value })
            }
          />
        </label>
        <label>
          Password
          <input
            type="password"
            required
            value={form.password}
            onChange={(event) =>
              setForm({ ...form, password: event.target.value })
            }
          />
        </label>
        <button className="button" disabled={busy}>
          {busy ? "Signing in..." : "Sign in"}
        </button>
        <p>
          New here? <Link to="/register">Create an account</Link>
        </p>
      </form>
    </section>
  );
}
