package ru.practicum.shareit.booking.service;


import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.exceptionhandler.BadRequestException;
import ru.practicum.shareit.exceptionhandler.NotFoundException;
import ru.practicum.shareit.exceptionhandler.WrongStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService implements IBookingService {

    private final IUserService iUserService;
    private final IItemRepository iItemRepository;
    private final IBookingRepository iBookingRepository;

    public BookingService(IUserService iUserService, IItemRepository iItemRepository, IBookingRepository iBookingRepository) {
        this.iUserService = iUserService;
        this.iItemRepository = iItemRepository;
        this.iBookingRepository = iBookingRepository;
    }

    @Override
    public List<BookingInfoDto> readAllUserBookings(int userId, String state) {
        Optional<User> user = iUserService.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundException("Юзера с ИД " + userId + " не существует");
        }

        if (user.get().getBookings().isEmpty()) {
            throw new NotFoundException("У юзера с ИД " + userId + " нет запросов на аренду");
        }


        if (Arrays.stream(BookingState.class.getEnumConstants()).anyMatch(e -> e.name().equals(state))) {
            List<Booking> bookingList = new ArrayList<>();

            switch (state) {
                case "ALL":
                    bookingList = iBookingRepository.findBookingsByUserId(userId);
                    break;

                case "CURRENT":
                    bookingList = iBookingRepository.findAllCurrentUserBookings(LocalDateTime.now(), userId);
                    break;

                case "PAST":
                    bookingList = iBookingRepository.findAllPastUserBookings(LocalDateTime.now(), userId);
                    break;

                case "FUTURE":
                    bookingList = iBookingRepository.findAllFutureUserBookings(LocalDateTime.now(), userId);
                    break;
            }

            return bookingList
                    .stream().map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());

        } else if (Arrays.stream(Status.class.getEnumConstants()).anyMatch(e -> e.name().equals(state))) {
            return iBookingRepository.findAllUserBookingsByStatus(Status.valueOf(state), userId).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        }

        throw new WrongStateException("Unknown state: UNSUPPORTED_STATUS");
    }

    @Override
    public BookingInfoDto read(int bookingId, int userId) {
        Optional<Booking> booking = iBookingRepository.findById(bookingId);
        Optional<User> user = iUserService.getUser(userId);

        if (booking.isEmpty()) {
            throw new NotFoundException("Не найден Booking с ИД " + bookingId);
        }

        if (user.isEmpty()) {
            throw new NotFoundException("Не найден Юзер с ИД " + userId);
        }

        if (booking.get().getUser().getId() == userId || booking.get().getItem().getUser().getId() == userId) {
            return BookingMapper.toBookingDto(booking.get());
        }

        throw new NotFoundException("Бронирование может просматривать только создатель или хозяин вещи");
    }

    @Override
    public List<BookingInfoDto> readBookingListOfAllUserItems(int ownerId, String state) {

        Optional<User> owner = iUserService.getUser(ownerId);

        if (owner.isEmpty()) {
            throw new NotFoundException("Юзера с ИД " + ownerId + " не существует");
        }

        if (owner.get().getItems().isEmpty()) {
            throw new NotFoundException("У юзера с ИД " + ownerId + " нет вещей для аренды");
        }

        if (Arrays.stream(BookingState.class.getEnumConstants()).anyMatch(e -> e.name().equals(state))) {

            List<Booking> bookingList = new ArrayList<>();

            switch (state) {
                case "ALL":
                    bookingList = iBookingRepository.findAllItemOwnerBookings(ownerId);
                    break;

                case "CURRENT":
                    bookingList = iBookingRepository.findAllItemOwnerCurrentBookings(LocalDateTime.now(), ownerId);
                    break;

                case "PAST":
                    bookingList = iBookingRepository.findAllItemOwnerPastBookings(LocalDateTime.now(), ownerId);
                    break;

                case "FUTURE":
                    bookingList = iBookingRepository.findAllItemOwnerFutureBookings(LocalDateTime.now(), ownerId);
                    break;
            }

            return bookingList
                    .stream().map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());

        } else if (Arrays.stream(Status.class.getEnumConstants()).anyMatch(e -> e.name().equals(state))) {
            return iBookingRepository.findAllItemOwnerBookingsByStatus(Status.valueOf(state), ownerId).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        }


        throw new WrongStateException("Unknown state: UNSUPPORTED_STATUS");

    }

    @Override
    public BookingInfoDto create(int userId, CreateBookingDto createBookingDto) {

        if (createBookingDto.getEnd().isBefore(createBookingDto.getStart())) {
            throw new BadRequestException("End Date is before Start Date");
        }

        Optional<User> user = iUserService.getUser(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User with ID " + userId + " not found");
        }

        Optional<Item> item = iItemRepository.findById(createBookingDto.getItemId());
        if (item.isEmpty()) {
            throw new NotFoundException("Item with ID " + createBookingDto.getItemId() + " not found");
        }

        if (!item.get().getAvailable()) {
            throw new BadRequestException("Item with ID " + createBookingDto.getItemId() + " not available");
        }

        if (userId == item.get().getUser().getId()) {
            throw new NotFoundException("Хозяин не может бронировать свои вещи");
        }

        if (!item.get().getBookings().isEmpty()) {
            checkTimeCrossing(createBookingDto);
        }

        Booking booking = BookingMapper.toBooking(createBookingDto, user.get(), item.get());
        Booking newBooking = iBookingRepository.save(booking);
        user.get().getBookings().add(newBooking);
        item.get().getBookings().add(newBooking);

        return BookingMapper.toBookingDto(newBooking);
    }

    @Override
    public BookingInfoDto bookingApproval(String approved, int bookingId, int userId) {
        Optional<Booking> bookingToApprove = iBookingRepository.findById(bookingId);

        if (bookingToApprove.isEmpty()) {
            throw new NotFoundException("Booking с ИД " + bookingId + " не найден");
        }

        if (!bookingToApprove.get().getStatus().equals(Status.WAITING)) {
            throw new BadRequestException("Booking с ИД " + bookingId + " статус уже изменен на " + bookingToApprove.get().getStatus());
        }

        if (bookingToApprove.get().getItem().getUser().getId() != userId) {
            throw new NotFoundException("Пользователь с ИД " + userId + " не собственник вещи");
        }

        switch (approved) {
            case "false":
                bookingToApprove.get().setStatus(Status.REJECTED);
                return BookingMapper.toBookingDto(iBookingRepository.save(bookingToApprove.get()));

            case "true":
                bookingToApprove.get().setStatus(Status.APPROVED);
                Booking approvedBooking = iBookingRepository.save(bookingToApprove.get());

                List<Booking> crossingBookings = iBookingRepository.getAllItemBookingsWithWaitingStatus(bookingId).stream()
                        .filter(f -> f.getStartDate().isAfter(bookingToApprove.get().getStartDate())
                                && f.getStartDate().isBefore(bookingToApprove.get().getEndDate()) ||
                                f.getEndDate().isAfter(bookingToApprove.get().getStartDate())
                                        && f.getEndDate().isBefore(bookingToApprove.get().getEndDate()) ||
                                f.getStartDate().isBefore(bookingToApprove.get().getStartDate())
                                        && f.getEndDate().isAfter(bookingToApprove.get().getEndDate()))
                        .collect(Collectors.toList());

                crossingBookings.stream().forEach(e -> e.setStatus(Status.REJECTED));
                iBookingRepository.saveAll(crossingBookings);

                return BookingMapper.toBookingDto(approvedBooking);

                default:
                    throw new BadRequestException("Должен быть либо false, либо true");
            }
    }

    @Override
    public boolean delete(int bookingId, int userId) {
        Optional<Booking> bookingToDelete = iBookingRepository.findById(bookingId);
        if (bookingToDelete.isEmpty() || bookingToDelete.get().getUser().getId() != userId) {
            return false;
        }
        iBookingRepository.delete(bookingToDelete.get());
        return true;
    }

    private void checkTimeCrossing(CreateBookingDto createBookingDto) {
        List<Booking> itemBookings = iBookingRepository.getAllItemBookingsWithApprovedStatus(createBookingDto.getItemId());
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
}
