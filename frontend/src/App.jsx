import { Routes, Route, Link } from "react-router-dom";
import Navbar from "./components/Navbar.jsx";
import Home from "./pages/Home.jsx";
import Hotels from "./pages/Hotels.jsx";
import HotelDetails from "./pages/HotelDetails.jsx";
import Booking from "./pages/Booking.jsx";
import Confirmation from "./pages/Confirmation.jsx";
import ManageBooking from "./pages/ManageBooking.jsx";

function App() {
  return (
    <div className="app-wrapper">
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/hotels" element={<Hotels />} />
          <Route path="/hotels/:id" element={<HotelDetails />} />
          <Route path="/book/:id" element={<Booking />} />
          <Route path="/confirmation" element={<Confirmation />} />
          <Route path="/manage-booking" element={<ManageBooking />} />
          <Route
            path="*"
            element={
              <div className="container py-5 text-center">
                <h2>Page not found</h2>
                <Link to="/" className="btn btn-primary mt-3">Go to Home</Link>
              </div>
            }
          />
        </Routes>
      </main>
      <footer className="footer">
        <div className="container text-center">StayEasy Hotel Booking Portal</div>
      </footer>
    </div>
  );
}

export default App;
