import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function SearchBar() {
  const [query, setQuery] = useState("");
  const navigate = useNavigate();

  function submit(event) {
    event.preventDefault();
    navigate(`/products${query ? `?search=${encodeURIComponent(query)}` : ""}`);
  }

  return (
    <form className="search-bar" onSubmit={submit} role="search">
      <label htmlFor="site-search">Search</label>
      <input
        id="site-search"
        value={query}
        onChange={(event) => setQuery(event.target.value)}
        placeholder="Search"
      />
      <button type="submit" aria-label="Submit search">
        ⌕
      </button>
    </form>
  );
}
