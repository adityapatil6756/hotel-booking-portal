package com.hotelbooking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String amenities;
    private BigDecimal pricePerNight;   // column price_per_night
    private Integer totalRooms;         // column total_rooms
    private String imageUrl;            // column image_url

    public Hotel() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }
    public Integer getTotalRooms() { return totalRooms; }
    public void setTotalRooms(Integer totalRooms) { this.totalRooms = totalRooms; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
