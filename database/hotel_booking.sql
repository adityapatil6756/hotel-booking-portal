-- Hotel Booking Portal - MySQL script

CREATE DATABASE IF NOT EXISTS hotel_booking_db;
USE hotel_booking_db;

-- Project user (safe to run again)
CREATE USER IF NOT EXISTS 'hotel_user'@'localhost' IDENTIFIED BY 'Hotel@1234';
GRANT ALL PRIVILEGES ON hotel_booking_db.* TO 'hotel_user'@'localhost';
FLUSH PRIVILEGES;

-- Drop old tables (children first) so the script can be re-run safely
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS room_availability;
DROP TABLE IF EXISTS hotels;

-- Table 1: hotels
CREATE TABLE hotels (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100)  NOT NULL,
    location        VARCHAR(100)  NOT NULL,
    description     TEXT,
    amenities       VARCHAR(255),
    price_per_night DECIMAL(10,2) NOT NULL,
    total_rooms     INT           NOT NULL,
    image_url       VARCHAR(500)
);

-- Table 2: room_availability (one row per hotel per night)
CREATE TABLE room_availability (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id     INT  NOT NULL,
    stay_date    DATE NOT NULL,
    booked_rooms INT  NOT NULL DEFAULT 0,
    UNIQUE KEY uq_hotel_date (hotel_id, stay_date),
    FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

-- Table 3: bookings
CREATE TABLE bookings (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    booking_reference VARCHAR(20)   NOT NULL UNIQUE,
    hotel_id          INT           NOT NULL,
    customer_name     VARCHAR(100)  NOT NULL,
    email             VARCHAR(150)  NOT NULL,
    check_in          DATE          NOT NULL,
    check_out         DATE          NOT NULL,
    total_amount      DECIMAL(10,2) NOT NULL,
    status            VARCHAR(20)   NOT NULL DEFAULT 'CONFIRMED',
    FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

-- Five sample hotels
INSERT INTO hotels (name, location, description, amenities, price_per_night, total_rooms, image_url) VALUES
('Sea Breeze Residency', 'Mumbai', 'A comfortable sea-facing hotel close to Marine Drive, perfect for business and leisure trips.', 'Free WiFi, Breakfast, Air Conditioning, Gym', 4500.00, 5, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&q=80'),
('Palm Beach Resort', 'Goa', 'A relaxing beach resort with a swimming pool, a short walk from the sand.', 'Swimming Pool, Free WiFi, Beach Access, Restaurant', 3800.00, 4, 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800&q=80'),
('Royal Heritage Haveli', 'Jaipur', 'A traditional haveli with royal decor, courtyard dining and city palace views.', 'Free WiFi, Breakfast, Courtyard, Spa', 3200.00, 3, 'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=800&q=80'),
('Garden City Inn', 'Bengaluru', 'A modern business hotel near MG Road with fast WiFi and meeting rooms.', 'Free WiFi, Breakfast, Parking, Meeting Room', 2800.00, 6, 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=800&q=80'),
('Snow Peak Lodge', 'Manali', 'A cozy mountain lodge with heated rooms and views of snow-covered peaks.', 'Heater, Free WiFi, Bonfire, Breakfast', 2000.00, 2, 'https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=800&q=80');

-- Check: this should show 5 hotels
SELECT * FROM hotels;