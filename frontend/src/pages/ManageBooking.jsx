import { useState } from "react";
import axios from "axios";
import BookingDetails from "../components/BookingDetails.jsx";
import TripChecklist from "../components/TripChecklist.jsx";
import { API_URL, getErrorMessage } from "../config.js";

function ManageBooking() {
  const [reference, setReference] = useState("");
  const [email, setEmail] = useState("");
  const [booking, setBooking] = useState(null);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  function handleFind(event) {
    event.preventDefault();
    setMessage("");
    setBooking(null);

    if (reference.trim() === "" || email.trim() === "") {
      setError("Please enter both booking reference and email.");
      return;
    }
    setError("");

    axios
      .get(API_URL + "/bookings/" + reference.trim(), { params: { email: email.trim() } })
      .then((response) => setBooking(response.data))
      .catch((err) => setError(getErrorMessage(err)));
  }

  function handleCancel() {
    const sure = window.confirm("Are you sure you want to cancel this booking?");
    if (!sure) {
      return;
    }
    setError("");

    axios
      .post(API_URL + "/bookings/" + booking.bookingReference + "/cancel", null, { params: { email: booking.email } })
      .then((response) => {
        setBooking(response.data);
        setMessage("Your booking has been cancelled and the room has been released.");
      })
      .catch((err) => setError(getErrorMessage(err)));
  }

  return (
    <div className="container py-4">
      <h2 className="mb-4">Manage your booking</h2>
      <div className="row g-4">
        <div className="col-12 col-lg-5">
          <div className="card">
            <div className="card-body">
              <form onSubmit={handleFind}>
                <label className="form-label">Booking reference</label>
                <input type="text" className="form-control mb-3" placeholder="HB-XXXXXXXX"
                  value={reference} onChange={(e) => setReference(e.target.value)} />
                <label className="form-label">Email address</label>
                <input type="email" className="form-control mb-3" placeholder="you@example.com"
                  value={email} onChange={(e) => setEmail(e.target.value)} />
                <button type="submit" className="btn btn-primary w-100">Find Booking</button>
              </form>
              {error !== "" && <div className="alert alert-danger mt-3 mb-0">{error}</div>}
            </div>
          </div>
        </div>

        <div className="col-12 col-lg-7">
          {message !== "" && <div className="alert alert-success">{message}</div>}
          {booking !== null && (
            <div>
              <BookingDetails booking={booking} />
              {booking.status === "CONFIRMED" && (
                <div>
                  <button className="btn btn-danger mt-3" onClick={handleCancel}>Cancel Booking</button>
                  <div className="mt-4"><TripChecklist /></div>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default ManageBooking;
