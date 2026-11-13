package com.hotelbooking.repository;

import com.hotelbooking.model.Hotel;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    // Spring writes the SQL for us from the method name (LIKE %location%)
    List<Hotel> findByLocationContainingIgnoreCase(String location);

    // Same as findById, but LOCKS the hotel row (SELECT ... FOR UPDATE).
    // Anyone else booking this hotel must wait until our transaction ends.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM Hotel h WHERE h.id = :id")
    Hotel findByIdForUpdate(@Param("id") Integer id);
}
