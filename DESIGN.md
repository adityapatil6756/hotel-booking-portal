# DESIGN.md

## 1. Architecture
Three parts talk in a chain:
1. **React** (browser, port 5173) shows pages and sends HTTP requests with Axios.
2. **Spring Boot** (port 8080) receives them. `Controller` reads the request, `Service` applies the rules, `Repository` talks to the database.
3. **MySQL** stores hotels, bookings and nightly room counts.

Data travels as JSON. CORS is enabled for `http://localhost:5173` on the controllers.

## 2. Database Design
- **hotels**: id, name, location, description, amenities, price_per_night, total_rooms, image_url.
- **bookings**: id, booking_reference (UNIQUE), hotel_id (FK), customer_name, email, check_in, check_out, total_amount, status (CONFIRMED / CANCELLED).
- **room_availability**: id, hotel_id (FK), stay_date, booked_rooms, UNIQUE(hotel_id, stay_date).

Relationships: one hotel has many bookings; one hotel has many availability rows (one per night).

**Adjustment from the assignment:** the suggested `available_rooms` column holds one number per hotel. That is wrong for date-based stays (booking 10–12 Nov must not reduce rooms for December). So the table stores **one row per hotel per night** with `booked_rooms`. Free rooms on a night = `total_rooms − booked_rooms`. Also added `image_url` to hotels because the UI needs hotel images.

## 3. API Design
See the table in README.md. Controllers are thin; all rules live in `BookingService`. Errors use one JSON shape (`GlobalExceptionHandler`): 400 validation, 404 not found, 409 no rooms, 500 unexpected.

## 4. Booking Logic
- **Dates:** check-in not in the past; check-out after check-in (checked in React for quick feedback and again in the backend, which is the real check).
- **Nights:** days between check-in and check-out. 10→13 Nov = 3 nights.
- **Total:** nights × price per night, calculated only in the backend (e.g. 3 × ₹2,000 = ₹6,000). React never sends a price.
- **Occupied nights:** a stay occupies check-in day up to, but not including, the check-out day.
- **Overlap:** Customer A 10–12 Nov and Customer B 11–13 Nov overlap because they both sleep on 11 Nov. This is the standard rule *existing check-in < new check-out AND existing check-out > new check-in*. Instead of counting overlapping bookings, we count rooms **per night**. It gives the same answer for a single stay but avoids a bug: stays 10–12 and 12–14 both "overlap" a 10–14 request but never at the same time, so counting bookings could wrongly reject it.
- **Availability:** a stay is possible only if on **every** night `booked_rooms < total_rooms`. Rooms shown = the lowest free count across the nights.
- **Booking:** adds 1 to `booked_rooms` for each night.
- **Cancellation:** subtracts 1 for each night and sets status `CANCELLED`. Cancelled bookings no longer count, so availability is released. Cancelling after the check-in date is blocked.
- **Assumption:** only CONFIRMED bookings occupy rooms (cancelled ones have already been subtracted). Check-out day is free for a new guest to check in.

## 5. Design Decisions
| Choice | Alternative | Reason / trade-off |
|---|---|---|
| Per-night table | Count overlapping bookings with SQL | Simple loops; easy to explain. Adds rows over time. |
| Pessimistic lock | Optimistic locking / unique constraints | Easiest to reason about. Bookings of one hotel are processed one at a time (fine at this scale). |
| Plain `hotelId` in availability entity | `@ManyToOne` | Fewer annotations. FK still enforced in MySQL. |
| `ddl-auto=none` + SQL script | Let Hibernate create tables | Schema is visible and submitted as SQL. |
| Bootstrap | Custom CSS only | Fast, responsive, professional. |
| `useState` only | Redux/Context | Small app; no need. |

## 6. Simultaneous Bookings
Problem: two people book the last room at the same moment. Both could read "1 room free" and both book it.
Solution: `createBooking` is `@Transactional` and first runs `findByIdForUpdate`, which locks the hotel row (`SELECT ... FOR UPDATE`). The second request waits at that line. When the first transaction commits, the second continues, reads the updated counts and gets a 409 "no rooms available". If anything fails, the whole transaction rolls back, so counts never end up half-updated.
Cancellation locks the booking row (so a double-click cannot release rooms twice) and the hotel row (so it does not clash with a new booking).
Status: implemented; verify using the PowerShell test in README (hotel 5 has 2 rooms; 4 parallel requests → 2 succeed).

## 7. Surprise Feature – Trip Preparation Checklist
- **Purpose:** after booking, remind the guest what to prepare for the trip.
- **Why chosen:** useful to real customers, small, and easy to explain, with no backend change.
- **How:** `TripChecklist.jsx` keeps a list of 5 items `{text, done}` in `useState`. Clicking a checkbox calls `toggleItem(position)`, which builds a new list with that item's `done` flipped. A counter and a Bootstrap progress bar show how many are ticked. Shown on the Confirmation page and on Manage Booking (confirmed bookings only).
- **Limit:** state is not saved; refresh resets it.

## 8. Limitations and Improvements
Not implemented: admin panel, authentication, payments, emails, multiple room types, several rooms per booking, saving checklist, integration tests against a real database, Docker.
With more time: add database integration tests for concurrency, admin screens for hotels/bookings, email confirmations, a saved checklist (new table or localStorage), pagination.
