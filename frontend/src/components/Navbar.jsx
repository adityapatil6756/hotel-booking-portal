import { useState } from "react";
import { Link } from "react-router-dom";

function Navbar() {
  const [open, setOpen] = useState(false);

  return (
    <nav className="navbar navbar-expand-md navbar-dark site-navbar">
      <div className="container">
        <Link className="navbar-brand fw-bold" to="/" onClick={() => setOpen(false)}>
          🏨 StayEasy
        </Link>
        <button className="navbar-toggler" type="button" onClick={() => setOpen(!open)}>
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className={"collapse navbar-collapse" + (open ? " show" : "")}>
          <ul className="navbar-nav ms-auto">
            <li className="nav-item">
              <Link className="nav-link" to="/" onClick={() => setOpen(false)}>Home</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/hotels" onClick={() => setOpen(false)}>Hotels</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/manage-booking" onClick={() => setOpen(false)}>Manage Booking</Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
