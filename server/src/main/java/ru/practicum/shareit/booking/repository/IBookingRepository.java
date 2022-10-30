package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.status = :status and b.user.id = :userId ORDER BY b.startDate desc")
    List<Booking> findAllUserBookingsByStatus(
            @Param("status") Status status,
            @Param("userId") Integer userId,
            Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.user.id = :ownerId and b.status = :status ORDER BY b.startDate desc")
    List<Booking> findAllItemOwnerBookingsByStatus(
            @Param("status") Status status,
            @Param("ownerId") Integer ownerId,
            Pageable pageable);

    @Query("select b from Booking b where b.user.id = :userId order by b.startDate desc")
    List<Booking> findBookingsByUserId(
            @Param("userId") Integer userId,
            Pageable pageable);

    @Query("select b from Booking b where b.user.id = :userId and b.startDate < :currentDate and b.endDate > :currentDate order by b.startDate desc")
    List<Booking> findAllCurrentUserBookings(
            @Param("currentDate") LocalDateTime currentDate,
            @Param("userId") Integer userId,
            Pageable pageable);

    @Query("select b from Booking b where b.user.id = :userId and b.endDate < :currentDate order by b.startDate desc")
    List<Booking> findAllPastUserBookings(
            @Param("currentDate") LocalDateTime currentDate,
            @Param("userId") Integer userId,
            Pageable pageable);

    @Query("select b from Booking b where b.user.id = :userId and b.startDate > :currentDate order by b.startDate desc")
    List<Booking> findAllFutureUserBookings(
            @Param("currentDate") LocalDateTime currentDate,
            @Param("userId") Integer userId,
            Pageable pageable);

    @Query("select b from Booking b where b.item.user.id = :ownerId order by b.startDate desc")
    List<Booking> findAllItemOwnerBookings(
            @Param("ownerId") Integer ownerId,
            Pageable pageable);

    @Query("select b from Booking b where b.item.user.id = :ownerId and b.startDate < :currentDate and b.endDate > :currentDate order by b.startDate desc")
    List<Booking> findAllItemOwnerCurrentBookings(
            @Param("currentDate") LocalDateTime currentDate,
            @Param("ownerId") Integer ownerId,
            Pageable pageable);

    @Query("select b from Booking b where b.item.user.id = :ownerId and b.endDate < :currentDate order by b.startDate desc")
    List<Booking> findAllItemOwnerPastBookings(
            @Param("currentDate") LocalDateTime currentDate,
            @Param("ownerId") Integer ownerId,
            Pageable pageable);

    @Query("select b from Booking b where b.item.user.id = :ownerId and b.startDate > :currentDate order by b.startDate desc")
    List<Booking> findAllItemOwnerFutureBookings(
            @Param("currentDate") LocalDateTime currentDate,
            @Param("ownerId") Integer ownerId,
            Pageable pageable);

    @Query("select b from Booking b where b.item.id = :itemId and b.status = 'WAITING'")
    List<Booking> getAllItemBookingsWithWaitingStatus(
            @Param("itemId") Integer itemId);

    @Query("select b from Booking b where b.item.id = :itemId and b.status = 'APPROVED'")
    List<Booking> getAllItemBookingsWithApprovedStatus(
            @Param("itemId") Integer itemId);

}
