import { useLocation, Link } from "react-router-dom";
import BookingDetails from "../components/BookingDetails.jsx";
import TripChecklist from "../components/TripChecklist.jsx";

function Confirmation() {
  const location = useLocation();
  const booking = location.state ? location.state.booking : null;

  // If the page was refreshed, the booking data is gone
  if (booking === null) {
    return (
      <div className="container py-5 text-center">
        <h3>No booking to show</h3>
        <p className="text-muted">Use "Manage Booking" with your reference and email to view a booking.</p>
        <Link to="/manage-booking" className="btn btn-primary">Manage Booking</Link>
      </div>
    );
  }

  return (
    <div className="container py-4">
      <div className="text-center mb-4">
        <div className="success-icon">✔</div>
        <h2>Booking Confirmed!</h2>
        <p className="text-muted">Please save your booking reference. You need it with your email to view or cancel the booking.</p>
      </div>
      <div className="row g-4 justify-content-center">
        <div className="col-12 col-lg-6"><BookingDetails booking={booking} /></div>
        <div className="col-12 col-lg-5"><TripChecklist /></div>
      </div>
      <div className="text-center mt-4">
        <Link to="/" className="btn btn-outline-primary">Back to Home</Link>
      </div>
    </div>
  );
}

export default Confirmation;
