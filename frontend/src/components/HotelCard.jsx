import { Link } from "react-router-dom";
import { FALLBACK_IMAGE, formatRupees } from "../config.js";

// Shows one hotel. Props: hotel, checkIn, checkOut (dates are optional)
function HotelCard({ hotel, checkIn, checkOut }) {
  let link = "/hotels/" + hotel.id;
  if (checkIn && checkOut) {
    link = link + "?checkIn=" + checkIn + "&checkOut=" + checkOut;
  }

  const amenityList = hotel.amenities ? hotel.amenities.split(",") : [];

  return (
    <div className="card hotel-card h-100">
      <img
        src={hotel.imageUrl}
        alt={hotel.name}
        className="hotel-card-img"
        onError={(e) => { e.target.src = FALLBACK_IMAGE; }}
      />
      <div className="card-body d-flex flex-column">
        <h5 className="card-title mb-1">{hotel.name}</h5>
        <p className="text-muted mb-2">📍 {hotel.location}</p>
        <div className="mb-3">
          {amenityList.slice(0, 3).map((item, index) => (
            <span key={index} className="badge amenity-badge me-1">{item.trim()}</span>
          ))}
        </div>
        <div className="mt-auto d-flex justify-content-between align-items-center">
          <div>
            <span className="price">{formatRupees(hotel.pricePerNight)}</span>
            <small className="text-muted"> / night</small>
          </div>
          <Link to={link} className="btn btn-primary btn-sm">View Details</Link>
        </div>
      </div>
    </div>
  );
}

export default HotelCard;
