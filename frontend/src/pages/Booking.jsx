import { useState, useEffect } from "react";
import { useParams, useSearchParams, useNavigate, Link } from "react-router-dom";
import axios from "axios";
import { API_URL, validateDates, getErrorMessage, formatRupees } from "../config.js";

function Booking() {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const checkIn = searchParams.get("checkIn") || "";
  const checkOut = searchParams.get("checkOut") || "";
  const navigate = useNavigate();

  const [summary, setSummary] = useState(null);   // hotel name, nights and total from the backend
  const [pageError, setPageError] = useState("");
  const [customerName, setCustomerName] = useState("");
  const [email, setEmail] = useState("");
  const [formError, setFormError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  // Ask the backend for the price summary and availability
  useEffect(() => {
    const message = validateDates(checkIn, checkOut);
    if (message !== "") {
      setPageError(message);
      return;
    }
    axios
      .get(API_URL + "/hotels/" + id + "/availability", { params: { checkIn: checkIn, checkOut: checkOut } })
      .then((response) => {
        if (!response.data.available) {
          setPageError("Sorry, no rooms are available for these dates.");
        }
        setSummary(response.data);
      })
      .catch((err) => setPageError(getErrorMessage(err)));
  }, [id, checkIn, checkOut]);

  function handleSubmit(event) {
    event.preventDefault();

    if (customerName.trim() === "") {
      setFormError("Please enter your name.");
      return;
    }
    if (email.trim() === "" || !email.includes("@") || !email.includes(".")) {
      setFormError("Please enter a valid email address.");
      return;
    }
    setFormError("");
    setSubmitting(true);

    const bookingData = {
      hotelId: Number(id),
      customerName: customerName.trim(),
      email: email.trim(),
      checkIn: checkIn,
      checkOut: checkOut,
    };

    axios
      .post(API_URL + "/bookings", bookingData)
      .then((response) => {
        navigate("/confirmation", { state: { booking: response.data } });
      })
      .catch((err) => {
        setFormError(getErrorMessage(err));
        setSubmitting(false);
      });
  }

  if (pageError !== "") {
    return (
      <div className="container py-5">
        <div className="alert alert-danger">{pageError}</div>
        <Link to={"/hotels/" + id} className="btn btn-primary">Back to hotel</Link>
      </div>
    );
  }
  if (summary === null) {
    return <div className="text-center py-5"><div className="spinner-border text-primary"></div></div>;
  }

  return (
    <div className="container py-4">
      <h2 className="mb-4">Complete your booking</h2>
      <div className="row g-4">
        <div className="col-12 col-md-7">
          <div className="card">
            <div className="card-body">
              <h5 className="mb-3">Guest details</h5>
              <form onSubmit={handleSubmit}>
                <label className="form-label">Full name</label>
                <input type="text" className="form-control mb-3" value={customerName}
                  onChange={(e) => setCustomerName(e.target.value)} placeholder="Your name" />
                <label className="form-label">Email address</label>
                <input type="email" className="form-control mb-3" value={email}
                  onChange={(e) => setEmail(e.target.value)} placeholder="you@example.com" />
                {formError !== "" && <div className="alert alert-danger">{formError}</div>}
                <button type="submit" className="btn btn-success w-100" disabled={submitting}>
                  {submitting ? "Booking..." : "Confirm Booking"}
                </button>
              </form>
            </div>
          </div>
        </div>

        <div className="col-12 col-md-5">
          <div className="card summary-card">
            <div className="card-body">
              <h5 className="mb-3">Booking summary</h5>
              <p className="fw-bold mb-1">{summary.hotelName}</p>
              <p className="mb-1">Check-in: {summary.checkIn}</p>
              <p className="mb-1">Check-out: {summary.checkOut}</p>
              <hr />
              <p className="mb-1">{formatRupees(summary.pricePerNight)} × {summary.nights} night(s)</p>
              <p className="fs-4 fw-bold mb-0">Total: {formatRupees(summary.totalAmount)}</p>
              <small className="text-muted">Final amount is calculated by the server.</small>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Booking;
