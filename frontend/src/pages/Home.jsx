import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import SearchBar from "../components/SearchBar.jsx";
import HotelCard from "../components/HotelCard.jsx";
import { API_URL, getErrorMessage } from "../config.js";

function Home() {
  const [hotels, setHotels] = useState([]);
  const [error, setError] = useState("");

  // Load hotels once when the page opens
  useEffect(() => {
    axios
      .get(API_URL + "/hotels")
      .then((response) => setHotels(response.data))
      .catch((err) => setError(getErrorMessage(err)));
  }, []);

  const featured = hotels.slice(0, 3);

  return (
    <div>
      <section className="hero">
        <div className="container">
          <h1 className="hero-title">Find your perfect stay</h1>
          <p className="hero-subtitle">Search hotels, check rooms and book in a few clicks. No account needed.</p>
          <SearchBar />
        </div>
      </section>

      <section className="container py-5">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h3 className="mb-0">Popular Hotels</h3>
          <Link to="/hotels" className="btn btn-outline-primary btn-sm">View all</Link>
        </div>
        {error !== "" && <div className="alert alert-danger">{error}</div>}
        <div className="row g-4">
          {featured.map((hotel) => (
            <div className="col-12 col-md-6 col-lg-4" key={hotel.id}>
              <HotelCard hotel={hotel} />
            </div>
          ))}
        </div>
      </section>

      <section className="features py-5">
        <div className="container">
          <div className="row text-center g-4">
            <div className="col-md-4"><div className="feature-icon">🔍</div><h5>Easy Search</h5><p className="text-muted">Search by location and dates.</p></div>
            <div className="col-md-4"><div className="feature-icon">💰</div><h5>Clear Pricing</h5><p className="text-muted">See the total amount before you confirm.</p></div>
            <div className="col-md-4"><div className="feature-icon">✅</div><h5>Free Cancellation</h5><p className="text-muted">Cancel any time before check-in.</p></div>
          </div>
        </div>
      </section>
    </div>
  );
}

export default Home;
