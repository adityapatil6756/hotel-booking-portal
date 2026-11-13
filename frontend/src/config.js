// Small helper values and functions used by many pages.

export const API_URL = "http://localhost:8080/api";

// Shown if a hotel image fails to load
export const FALLBACK_IMAGE =
  "data:image/svg+xml;utf8," +
  encodeURIComponent(
    "<svg xmlns='http://www.w3.org/2000/svg' width='800' height='500'><rect width='100%' height='100%' fill='#dde5ee'/><text x='50%' y='50%' font-size='32' fill='#6b7c93' text-anchor='middle' font-family='Arial'>Hotel Image</text></svg>"
  );

// Today's date as yyyy-MM-dd (used as the minimum value of date inputs)
export function getTodayString() {
  const now = new Date();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");
  return now.getFullYear() + "-" + month + "-" + day;
}

// Returns an error message, or "" if the dates are fine
export function validateDates(checkIn, checkOut) {
  if (!checkIn || !checkOut) {
    return "Please select both check-in and check-out dates.";
  }
  if (checkIn < getTodayString()) {
    return "Check-in date cannot be in the past.";
  }
  if (checkOut <= checkIn) {
    return "Check-out date must be after check-in date.";
  }
  return "";
}

// Gets the message sent by our backend, or a friendly fallback
export function getErrorMessage(error) {
  if (error.response && error.response.data && error.response.data.message) {
    return error.response.data.message;
  }
  return "Cannot connect to the server. Please make sure the backend is running.";
}

export function formatRupees(amount) {
  return "₹" + Number(amount).toLocaleString("en-IN");
}
