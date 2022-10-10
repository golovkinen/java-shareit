package ru.practicum.shareit.booking.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.status = :status and b.user.id = :userId ORDER BY b.startDate desc")
    List<Booking> findAllUserBookingsByStatus(
            @Param("status") Status status,
            @Param("userId") Integer userId);

    @Query("SELECT b FROM Booking b WHERE b.item.user.id = :ownerId and b.status = :status ORDER BY b.startDate desc")
    List<Booking> findAllItemOwnerBookingsByStatus(
            @Param("status") Status status,
            @Param("ownerId") Integer ownerId);
}
