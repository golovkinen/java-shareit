package ru.practicum.shareit.booking.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.exceptionhandler.BadRequestException;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BookingRepositoryCustom implements IBookingRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    public void checkTimeCrossing(CreateBookingDto createBookingDto) {
        List<Booking> itemBookings = getAllItemBookingsWithAcceptedStatus(createBookingDto.getItemId());
        if (!itemBookings.isEmpty() && itemBookings.stream()
                .anyMatch(f -> f.getStartDate().isBefore(createBookingDto.getEnd()) &&
                        f.getStartDate().isAfter(createBookingDto.getStart()) ||
                        f.getEndDate().isBefore(createBookingDto.getEnd()) &&
                                f.getEndDate().isAfter(createBookingDto.getStart()) ||
                        f.getStartDate().isBefore(createBookingDto.getStart()) &&
                                f.getEndDate().isAfter(createBookingDto.getEnd()))) {
            throw new BadRequestException("В период с " + createBookingDto.getStart() + " по "
                    + createBookingDto.getEnd() + " вещь с ИД " + createBookingDto.getItemId() + " недоступна");
        }
    }

    @Override
    public List<Booking> getAllUserBookingsByDateState(int userId, String state) {

        switch (state) {
            case "ALL":
                return entityManager.createQuery("select b from Booking b where b.user.id = :userId " +
                                "order by b.startDate desc")
                        .setParameter("userId", userId)
                        .getResultList();

            case "CURRENT":
                return entityManager.createQuery("select b from Booking b where b.user.id = :userId " +
                                "and b.startDate < :currentDate and b.endDate > :currentDate order by b.startDate desc")
                        .setParameter("userId", userId)
                        .setParameter("currentDate", LocalDateTime.now())
                        .getResultList();

            case "PAST":
                return entityManager.createQuery("select b from Booking b where b.user.id = :userId " +
                                "and b.endDate < :currentDate order by b.startDate desc")
                        .setParameter("userId", userId)
                        .setParameter("currentDate", LocalDateTime.now())
                        .getResultList();

            case "FUTURE":
                return entityManager.createQuery("select b from Booking b where b.user.id = :userId " +
                                "and b.startDate > :currentDate order by b.startDate desc")
                        .setParameter("userId", userId)
                        .setParameter("currentDate", LocalDateTime.now())
                        .getResultList();
        }
        return null;
    }

    @Override
    public List<Booking> getAllOwnerItemsBookingsByDateState(int ownerId, String state) {
        switch (state) {
            case "ALL":
                return entityManager.createQuery("select b from Booking b where b.item.user.id = :ownerId " +
                                "order by b.startDate desc")
                        .setParameter("ownerId", ownerId)
                        .getResultList();

            case "CURRENT":
                return entityManager.createQuery("select b from Booking b where b.item.user.id = :ownerId " +
                                "and b.startDate < :currentDate and b.endDate > :currentDate order by b.startDate desc")
                        .setParameter("ownerId", ownerId)
                        .setParameter("currentDate", LocalDateTime.now())
                        .getResultList();

            case "PAST":
                return entityManager.createQuery("select b from Booking b where b.item.user.id = :ownerId " +
                                "and b.endDate < :currentDate order by b.startDate desc")
                        .setParameter("ownerId", ownerId)
                        .setParameter("currentDate", LocalDateTime.now())
                        .getResultList();

            case "FUTURE":
                return entityManager.createQuery("select b from Booking b where b.item.user.id = :ownerId " +
                                "and b.startDate > :currentDate order by b.startDate desc")
                        .setParameter("ownerId", ownerId)
                        .setParameter("currentDate", LocalDateTime.now())
                        .getResultList();
        }
        return null;
    }

    @Override
    public List<Booking> getAllItemBookingsWithWaitingStatus(int itemId) {
        return entityManager.createQuery("select b from Booking b where b.item.id = :itemId and b.status = 'WAITING'")
                .setParameter("itemId", itemId)
                .getResultList();
    }

    private List<Booking> getAllItemBookingsWithAcceptedStatus(int itemId) {
        return entityManager.createQuery("select b from Booking b where b.item.id = :itemId and b.status = 'APPROVED'")
                .setParameter("itemId", itemId)
                .getResultList();
    }
}
