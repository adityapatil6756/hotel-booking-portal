import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import axios from "axios";
import SearchBar from "../components/SearchBar.jsx";
import HotelCard from "../components/HotelCard.jsx";
import { API_URL, getErrorMessage } from "../config.js";

function Hotels() {
  const [searchParams] = useSearchParams();
  const location = searchParams.get("location") || "";
  const checkIn = searchParams.get("checkIn") || "";
  const checkOut = searchParams.get("checkOut") || "";

  const [hotels, setHotels] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Runs again whenever the search values in the URL change
  useEffect(() => {
    setLoading(true);
    setError("");

    // Only send values that are filled in
    const params = {};
    if (location) params.location = location;
    if (checkIn) params.checkIn = checkIn;
    if (checkOut) params.checkOut = checkOut;

    axios
      .get(API_URL + "/hotels/search", { params: params })
      .then((response) => setHotels(response.data))
      .catch((err) => {
        setHotels([]);
        setError(getErrorMessage(err));
      })
      .finally(() => setLoading(false));
  }, [location, checkIn, checkOut]);

  return (
    <div className="container py-4">
      <h2 className="mb-3">Hotels</h2>
      <div className="mb-4">
        <SearchBar
          key={location + checkIn + checkOut}
          initialLocation={location}
          initialCheckIn={checkIn}
          initialCheckOut={checkOut}
        />
      </div>

      {loading && <div className="text-center py-5"><div className="spinner-border text-primary"></div></div>}
      {error !== "" && <div className="alert alert-danger">{error}</div>}

      {!loading && error === "" && hotels.length === 0 && (
        <div className="alert alert-warning">No hotels found. Try a different location or different dates.</div>
      )}

      <div className="row g-4">
        {hotels.map((hotel) => (
          <div className="col-12 col-md-6 col-lg-4" key={hotel.id}>
            <HotelCard hotel={hotel} checkIn={checkIn} checkOut={checkOut} />
          </div>
        ))}
      </div>
    </div>
  );
}

export default Hotels;
