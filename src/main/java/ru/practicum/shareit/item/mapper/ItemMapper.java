package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemMapper {
    public static Item toItem(ItemDto itemDto, User user) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                user, new HashSet<>(), new HashSet<>());
    }

    public static ItemInfoDto toItemDto(Item item, Optional<Booking> lastBooking, Optional<Booking> nextBooking) {

        if (lastBooking.isEmpty() && nextBooking.isEmpty() && item.getComments().isEmpty()) {
            return new ItemInfoDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                    null,
                    null,
                    new ArrayList<>());
        }

        if (lastBooking.isEmpty() && nextBooking.isEmpty() && !item.getComments().isEmpty()) {
            return new ItemInfoDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                    null,
                    null,
                    item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        }

        if (lastBooking.isEmpty() && nextBooking.isPresent()) {
            return new ItemInfoDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                    null,
                    new ItemInfoDto.BookingInfoForItemDto(nextBooking.get().getBookingId(), nextBooking.get().getUser().getId()),
                    new ArrayList<>());
        }

        if (lastBooking.isPresent() && nextBooking.isEmpty() && item.getComments().isEmpty()) {

            return new ItemInfoDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                    new ItemInfoDto.BookingInfoForItemDto(lastBooking.get().getBookingId(), lastBooking.get().getUser().getId()),
                    null,
                    new ArrayList<>());
        }

        if (lastBooking.isPresent() && nextBooking.isEmpty() && !item.getComments().isEmpty()) {

            return new ItemInfoDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                    new ItemInfoDto.BookingInfoForItemDto(lastBooking.get().getBookingId(), lastBooking.get().getUser().getId()),
                    null,
                    item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        }

        if (lastBooking.isPresent() && nextBooking.isPresent() && item.getComments().isEmpty()) {
            return new ItemInfoDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                    new ItemInfoDto.BookingInfoForItemDto(lastBooking.get().getBookingId(), lastBooking.get().getUser().getId()),
                    new ItemInfoDto.BookingInfoForItemDto(nextBooking.get().getBookingId(), nextBooking.get().getUser().getId()),
                    new ArrayList<>());
        }

        return new ItemInfoDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                new ItemInfoDto.BookingInfoForItemDto(lastBooking.get().getBookingId(), lastBooking.get().getUser().getId()),
                new ItemInfoDto.BookingInfoForItemDto(nextBooking.get().getBookingId(), nextBooking.get().getUser().getId()),
                item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));

    }
}
