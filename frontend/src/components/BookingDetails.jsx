import { formatRupees } from "../config.js";

// Shows booking information. Used on the Confirmation page and the Manage Booking page.
function BookingDetails({ booking }) {
  const isCancelled = booking.status === "CANCELLED";

  return (
    <div className="card booking-card">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-start flex-wrap mb-3">
          <div>
            <small className="text-muted">Booking Reference</small>
            <div className="reference">{booking.bookingReference}</div>
          </div>
          <span className={"badge fs-6 " + (isCancelled ? "bg-danger" : "bg-success")}>{booking.status}</span>
        </div>
        <table className="table table-borderless mb-0">
          <tbody>
            <tr><th>Hotel</th><td>{booking.hotel.name}, {booking.hotel.location}</td></tr>
            <tr><th>Guest</th><td>{booking.customerName}</td></tr>
            <tr><th>Email</th><td>{booking.email}</td></tr>
            <tr><th>Check-in</th><td>{booking.checkIn}</td></tr>
            <tr><th>Check-out</th><td>{booking.checkOut}</td></tr>
            <tr><th>Total Amount</th><td className="fw-bold">{formatRupees(booking.totalAmount)}</td></tr>
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default BookingDetails;
