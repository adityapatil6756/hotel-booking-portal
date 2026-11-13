import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { getTodayString, validateDates } from "../config.js";

// Search form. On submit it goes to /hotels?location=...&checkIn=...&checkOut=...
function SearchBar({ initialLocation = "", initialCheckIn = "", initialCheckOut = "" }) {
  const [location, setLocation] = useState(initialLocation);
  const [checkIn, setCheckIn] = useState(initialCheckIn);
  const [checkOut, setCheckOut] = useState(initialCheckOut);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  function handleSubmit(event) {
    event.preventDefault();

    // Dates are optional, but if one is chosen, both must be valid
    if (checkIn || checkOut) {
      const message = validateDates(checkIn, checkOut);
      if (message !== "") {
        setError(message);
        return;
      }
    }
    setError("");

    const params = new URLSearchParams();
    if (location.trim() !== "") params.set("location", location.trim());
    if (checkIn) params.set("checkIn", checkIn);
    if (checkOut) params.set("checkOut", checkOut);
    navigate("/hotels?" + params.toString());
  }

  return (
    <form className="search-card" onSubmit={handleSubmit}>
      <div className="row g-3 align-items-end">
        <div className="col-12 col-md-4">
          <label className="form-label">Location</label>
          <input
            type="text"
            className="form-control"
            placeholder="e.g. Goa"
            value={location}
            onChange={(e) => setLocation(e.target.value)}
          />
        </div>
        <div className="col-6 col-md-3">
          <label className="form-label">Check-in</label>
          <input
            type="date"
            className="form-control"
            min={getTodayString()}
            value={checkIn}
            onChange={(e) => setCheckIn(e.target.value)}
          />
        </div>
        <div className="col-6 col-md-3">
          <label className="form-label">Check-out</label>
          <input
            type="date"
            className="form-control"
            min={checkIn || getTodayString()}
            value={checkOut}
            onChange={(e) => setCheckOut(e.target.value)}
          />
        </div>
        <div className="col-12 col-md-2">
          <button type="submit" className="btn btn-primary w-100">Search</button>
        </div>
      </div>
      {error !== "" && <div className="alert alert-danger mt-3 mb-0">{error}</div>}
    </form>
  );
}

export default SearchBar;
