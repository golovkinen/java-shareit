package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.ItemInfoForBookingDto;
import ru.practicum.shareit.booking.dto.UserInfoDto;
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
                new UserInfoDto(booking.getUser().getId()),
                new ItemInfoForBookingDto(booking.getItem().getId(), booking.getItem().getName()));
    }
}
