# Interview Preparation

## 2-minute project explanation
"I built a Hotel Booking Portal with React, Spring Boot and MySQL. A customer can search hotels by location and dates, open a hotel, check room availability, see the total price and book with name and email. They get a unique booking reference and can later retrieve or cancel the booking using the reference and email.
The backend follows Controller, Service, Repository layers. The service calculates the price (nights × price per night) and checks availability. Availability is stored night by night in a table, so a booking for 10–12 November does not affect December. For each night I check booked rooms are less than total rooms. To stop two people booking the last room, the booking runs in a transaction and locks the hotel row, so the second request waits and then sees the updated count. Cancelling subtracts the rooms again. As an extra feature I added a trip checklist using React checkboxes. I wrote JUnit tests for price, dates and availability rules. I used AI assistance, and I went through the code to understand it."

## Flow
Customer → React page → Axios request → Controller → Service (rules) → Repository → MySQL → result back → Service → Controller (JSON) → React shows it.

## Why these technologies
- **React:** page built from small components; state updates the screen automatically.
- **Java:** strongly typed, widely used in companies, good for business logic.
- **Spring Boot:** quick setup for REST APIs, database access and validation.
- **MySQL:** relational; bookings and hotels have clear relationships; supports transactions and locks.
- **REST API:** frontend and backend are separate and talk with simple HTTP + JSON.

## 30+ technical questions (short answers)
**Java / OOP**
1. What is a class/object? A class is a blueprint (Hotel); an object is one real instance.
2. What is a constructor? Special method that runs when an object is created.
3. Why getters/setters? To read/change private fields safely (encapsulation).
4. What is encapsulation? Hiding data (private fields) and exposing it through methods.
5. What is an exception? An error at runtime. I use `BookingException` and try to give clear messages.
6. ArrayList vs array? ArrayList can grow; array has fixed size. I use ArrayList for nights.
7. `final` field? Cannot be reassigned; my services hold repositories as final.

**Spring Boot / REST**
8. What is Spring Boot? Framework that auto-configures a Spring app so I can focus on code.
9. `@RestController`? Class that handles HTTP requests and returns JSON.
10. `@Service`? Marks business-logic class.
11. `@Repository` / JpaRepository? Database access; gives findAll, findById, save, deleteById.
12. `@Entity`? Java class mapped to a table.
13. Dependency injection? Spring creates and passes objects (repositories) into my constructors.
14. `@RequestParam` vs `@PathVariable`? Query value (`?email=`) vs value in the URL path (`/{id}`).
15. GET vs POST? GET reads data; POST sends data to create/change something.
16. Status codes? 200 OK, 201 Created, 400 bad input, 404 not found, 409 conflict (no rooms), 500 server error.
17. `@Valid`? Runs checks like @NotBlank and @Email on the request body.
18. How do you handle errors? `@RestControllerAdvice` turns exceptions into one JSON format.
19. What is CORS? Browser rule blocking calls between different origins; I allow localhost:5173.
20. `@Transactional`? All DB changes succeed together or are rolled back.

**React**
21. What is a component? A reusable function returning UI (JSX).
22. `useState`? Stores data that changes; changing it re-renders the page.
23. `useEffect`? Runs code after render, e.g. loading hotels when the page opens.
24. Props? Data passed from parent to child (`<HotelCard hotel={...} />`).
25. Why `key` in lists? Helps React track each item.
26. React Router? Moves between pages without reloading.
27. Axios? Library for HTTP calls.

**MySQL**
28. Primary key / foreign key? Unique row id / a column pointing to another table's primary key (bookings.hotel_id → hotels.id).
29. JOIN? Combines rows of two tables, e.g. bookings with hotel names.
30. Why a UNIQUE constraint on booking_reference? Two bookings can never share a reference.
31. Why UNIQUE(hotel_id, stay_date)? Only one count row per hotel per night.

**Project logic**
32. Total price? Nights × price per night, calculated in the backend.
33. Why not trust the frontend price? Users can edit requests; the server is the source of truth.
34. How are nights counted? Check-out day is not counted; 10→13 = 3 nights.
35. How do you avoid overbooking? Per-night counts + transaction + row lock.
36. What is overlap? Two stays share at least one night.
37. What happens on cancel? Status = CANCELLED and booked_rooms −1 for each night.
38. How is a booking retrieved? Reference and email must both match.
39. Why a JUnit test without a database? The logic methods are plain Java, so tests are fast.

## Practical questions: how to answer
- **Why this project?** It was my interview assignment, and it covers frontend, backend and database together.
- **Your role?** I built it with AI assistance; I studied each file and can explain and change it. (Be honest.)
- **Challenges?** Understanding date-wise availability and why a single `available_rooms` number fails; handling two users booking at once.
- **How does booking work?** Validate dates → lock hotel → check each night → add 1 to each night → calculate price → save booking → return reference.
- **Duplicate bookings prevented?** Lock + transaction; the second request waits then sees updated counts.
- **Cancel?** Find by reference and email, check not already cancelled, subtract 1 per night, set CANCELLED.
- **Why MySQL?** Relational data and transaction support.
- **Improve?** Admin panel, multiple room types, email confirmation, more tests.
- **Most difficult?** Concurrency and availability logic. (Say what you honestly found hardest.)
