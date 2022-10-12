package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBooking(CreateBookingDto bookingDto, User user, Item item) {
        return new Booking(null, bookingDto.getStart(), bookingDto.getEnd(), Status.WAITING,
                item, user);
    }

    public static BookingInfoDto toBookingDto(Booking booking) {
        return new BookingInfoDto(booking.getBookingId(), booking.getStartDate(), booking.getEndDate(),
                booking.getStatus(),
                new BookingInfoDto.UserInfoDto(booking.getUser().getId()),
                new BookingInfoDto.ItemInfoForBookingDto(booking.getItem().getId(), booking.getItem().getName()));
    }
}
