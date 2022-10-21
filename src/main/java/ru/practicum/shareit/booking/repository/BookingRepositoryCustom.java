package ru.practicum.shareit.booking.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BookingRepositoryCustom implements IBookingRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Booking> findAllUserBookingsByStatus(Status status, int userId, int from, int size) {
        return entityManager.createQuery("select b from Booking b where b.user.id = :userId " +
                        "and b.status = :status order by b.startDate desc")
                .setParameter("userId", userId)
                .setParameter("status", status)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Booking> findAllItemOwnerBookingsByStatus(Status status, int ownerId, int from, int size) {
        return entityManager.createQuery("select b from Booking b where b.item.user.id = :ownerId " +
                        "and b.status = :status order by b.startDate desc")
                .setParameter("ownerId", ownerId)
                .setParameter("status", status)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Booking> findBookingsByUserId(int userId, int from, int size) {
        return entityManager.createQuery("select b from Booking b where b.user.id = :userId " +
                        "order by b.startDate desc")
                .setParameter("userId", userId)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Booking> findAllCurrentUserBookings(int userId, int from, int size) {
        return entityManager.createQuery("select b from Booking b where b.user.id = :userId " +
                        "and b.startDate < :currentDate and b.endDate > :currentDate order by b.startDate desc")
                .setParameter("userId", userId)
                .setParameter("currentDate", LocalDateTime.now())
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Booking> findAllPastUserBookings(int userId, int from, int size) {
        return entityManager.createQuery("select b from Booking b where b.user.id = :userId " +
                        "and b.endDate < :currentDate order by b.startDate desc")
                .setParameter("userId", userId)
                .setParameter("currentDate", LocalDateTime.now())
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Booking> findAllFutureUserBookings(int userId, int from, int size) {
        return entityManager.createQuery("select b from Booking b where b.user.id = :userId " +
                        "and b.startDate > :currentDate order by b.startDate desc")
                .setParameter("userId", userId)
                .setParameter("currentDate", LocalDateTime.now())
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Booking> findAllItemOwnerBookings(int ownerId, int from, int size) {
        return entityManager.createQuery("select b from Booking b where b.item.user.id = :ownerId " +
                        "order by b.startDate desc")
                .setParameter("ownerId", ownerId)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Booking> findAllItemOwnerCurrentBookings(int ownerId, int from, int size) {
        return entityManager.createQuery("select b from Booking b where b.item.user.id = :ownerId " +
                        "and b.startDate < :currentDate and b.endDate > :currentDate order by b.startDate desc")
                .setParameter("ownerId", ownerId)
                .setParameter("currentDate", LocalDateTime.now())
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Booking> findAllItemOwnerPastBookings(int ownerId, int from, int size) {
        return entityManager.createQuery("select b from Booking b where b.item.user.id = :ownerId " +
                        "and b.endDate < :currentDate order by b.startDate desc")
                .setParameter("ownerId", ownerId)
                .setParameter("currentDate", LocalDateTime.now())
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Booking> findAllItemOwnerFutureBookings(int ownerId, int from, int size) {
        return entityManager.createQuery("select b from Booking b where b.item.user.id = :ownerId " +
                        "and b.startDate > :currentDate order by b.startDate desc")
                .setParameter("ownerId", ownerId)
                .setParameter("currentDate", LocalDateTime.now())
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }
}
