import { useState, useEffect } from "react";
import { useParams, useSearchParams, Link } from "react-router-dom";
import axios from "axios";
import { API_URL, FALLBACK_IMAGE, getTodayString, validateDates, getErrorMessage, formatRupees } from "../config.js";

function HotelDetails() {
  const { id } = useParams();
  const [searchParams] = useSearchParams();

  const [hotel, setHotel] = useState(null);
  const [checkIn, setCheckIn] = useState(searchParams.get("checkIn") || "");
  const [checkOut, setCheckOut] = useState(searchParams.get("checkOut") || "");
  const [availability, setAvailability] = useState(null);
  const [error, setError] = useState("");
  const [pageError, setPageError] = useState("");

  useEffect(() => {
    axios
      .get(API_URL + "/hotels/" + id)
      .then((response) => setHotel(response.data))
      .catch((err) => setPageError(getErrorMessage(err)));
  }, [id]);

  function handleCheckAvailability() {
    setAvailability(null);
    const message = validateDates(checkIn, checkOut);
    if (message !== "") {
      setError(message);
      return;
    }
    setError("");

    axios
      .get(API_URL + "/hotels/" + id + "/availability", { params: { checkIn: checkIn, checkOut: checkOut } })
      .then((response) => setAvailability(response.data))
      .catch((err) => setError(getErrorMessage(err)));
  }

  if (pageError !== "") {
    return (
      <div className="container py-5">
        <div className="alert alert-danger">{pageError}</div>
        <Link to="/hotels" className="btn btn-primary">Back to hotels</Link>
      </div>
    );
  }
  if (hotel === null) {
    return <div className="text-center py-5"><div className="spinner-border text-primary"></div></div>;
  }

  const amenityList = hotel.amenities ? hotel.amenities.split(",") : [];

  return (
    <div className="container py-4">
      <Link to="/hotels" className="text-decoration-none">← Back to hotels</Link>
      <div className="row g-4 mt-1">
        <div className="col-12 col-lg-7">
          <img
            src={hotel.imageUrl}
            alt={hotel.name}
            className="details-img"
            onError={(e) => { e.target.src = FALLBACK_IMAGE; }}
          />
          <h2 className="mt-3 mb-1">{hotel.name}</h2>
          <p className="text-muted">📍 {hotel.location}</p>
          <p>{hotel.description}</p>
          <h6>Amenities</h6>
          <div className="mb-3">
            {amenityList.map((item, index) => (
              <span key={index} className="badge amenity-badge me-1 mb-1">{item.trim()}</span>
            ))}
          </div>
          <p className="price fs-4">{formatRupees(hotel.pricePerNight)} <small className="text-muted fs-6">per night</small></p>
        </div>

        <div className="col-12 col-lg-5">
          <div className="card booking-panel">
            <div className="card-body">
              <h5 className="mb-3">Check availability</h5>
              <label className="form-label">Check-in</label>
              <input type="date" className="form-control mb-3" min={getTodayString()} value={checkIn}
                onChange={(e) => { setCheckIn(e.target.value); setAvailability(null); }} />
              <label className="form-label">Check-out</label>
              <input type="date" className="form-control mb-3" min={checkIn || getTodayString()} value={checkOut}
                onChange={(e) => { setCheckOut(e.target.value); setAvailability(null); }} />
              <button className="btn btn-primary w-100" onClick={handleCheckAvailability}>Check Rooms</button>

              {error !== "" && <div className="alert alert-danger mt-3 mb-0">{error}</div>}

              {availability !== null && availability.available && (
                <div className="alert alert-success mt-3 mb-0">
                  <strong>{availability.availableRooms} room(s) available</strong>
                  <div>{availability.nights} night(s) × {formatRupees(availability.pricePerNight)}</div>
                  <div className="fs-5 fw-bold">Total: {formatRupees(availability.totalAmount)}</div>
                  <Link
                    to={"/book/" + id + "?checkIn=" + checkIn + "&checkOut=" + checkOut}
                    className="btn btn-success w-100 mt-2"
                  >
                    Book Now
                  </Link>
                </div>
              )}
              {availability !== null && !availability.available && (
                <div className="alert alert-warning mt-3 mb-0">No rooms available for these dates. Please try other dates.</div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default HotelDetails;
