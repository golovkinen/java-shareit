package ru.practicum.shareit.booking.model;


import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.exceptionhandler.BadRequestException;
import ru.practicum.shareit.exceptionhandler.NotFoundException;
import ru.practicum.shareit.exceptionhandler.WrongStateException;
import ru.practicum.shareit.item.model.IItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.IUserService;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService implements IBookingService {

    private final IUserService iUserService;
    private final IItemRepository iItemRepository;
    private final IBookingRepository iBookingRepository;
    private final IBookingRepositoryCustom iBookingRepositoryCustom;

    public BookingService(IUserService iUserService, IItemRepository iItemRepository, IBookingRepository iBookingRepository, IBookingRepositoryCustom iBookingRepositoryCustom) {
        this.iUserService = iUserService;
        this.iItemRepository = iItemRepository;
        this.iBookingRepository = iBookingRepository;
        this.iBookingRepositoryCustom = iBookingRepositoryCustom;
    }

    @Override
    public Optional<List<BookingInfoDto>> readAllUserBookings(int userId, String state) {
        Optional<User> user = iUserService.getUser(userId);

        if (user.isEmpty() || user.get().getBookings().isEmpty()) {
            return Optional.empty();
        }


        if (Arrays.stream(StateInTime.class.getEnumConstants()).anyMatch(e -> e.name().equals(state))) {
            return Optional.of(iBookingRepositoryCustom.getAllUserBookingsByDateState(userId, state)
                    .stream().map(f -> BookingMapper.toBookingDto(f))
                    .collect(Collectors.toList()));
        } else if (Arrays.stream(Status.class.getEnumConstants()).anyMatch(e -> e.name().equals(state))) {
            return Optional.of(iBookingRepository.findAllUserBookingsByStatus(Status.valueOf(state), userId).stream()
                    .map(f -> BookingMapper.toBookingDto(f))
                    .collect(Collectors.toList()));
        }

        throw new WrongStateException("Unknown state: UNSUPPORTED_STATUS");
    }

    @Override
    public Optional<BookingInfoDto> read(int bookingId, int userId) {
        Optional<Booking> booking = iBookingRepository.findById(bookingId);
        Optional<User> user = iUserService.getUser(userId);

        if (booking.isEmpty() || user.isEmpty()) {
            return Optional.empty();
        }

        if (booking.get().getUser().getId() == userId || booking.get().getItem().getUser().getId() == userId) {
            return Optional.of(BookingMapper.toBookingDto(booking.get()));
        }

        return Optional.empty();
    }

    @Override
    public Optional<List<BookingInfoDto>> readBookingListOfAllUserItems(int ownerId, String state) {

        Optional<User> owner = iUserService.getUser(ownerId);

        if (owner.isEmpty() || owner.get().getItems().isEmpty()) {
            return Optional.empty();
        }

        if (Arrays.stream(StateInTime.class.getEnumConstants()).anyMatch(e -> e.name().equals(state))) {
            return Optional.of(iBookingRepositoryCustom.getAllOwnerItemsBookingsByDateState(ownerId, state)
                    .stream().map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList()));
        } else if (Arrays.stream(Status.class.getEnumConstants()).anyMatch(e -> e.name().equals(state))) {
            return Optional.of(iBookingRepository.findAllItemOwnerBookingsByStatus(Status.valueOf(state), ownerId).stream()
                    .map(f -> BookingMapper.toBookingDto(f))
                    .collect(Collectors.toList()));
        }


        throw new WrongStateException("Unknown state: UNSUPPORTED_STATUS");

    }

    @Override
    public Optional<BookingInfoDto> create(int userId, CreateBookingDto createBookingDto) {

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
            iBookingRepositoryCustom.checkTimeCrossing(createBookingDto);
        }

        Booking booking = BookingMapper.toBooking(createBookingDto, user.get(), item.get());
        Booking newBooking = iBookingRepository.save(booking);
        user.get().getBookings().add(newBooking);
        item.get().getBookings().add(newBooking);

        return Optional.of(BookingMapper.toBookingDto(newBooking));
    }

    @Override
    public boolean bookingApproval(String approved, int bookingId, int userId) {
        Optional<Booking> bookingToApprove = iBookingRepository.findById(bookingId);
        if (!bookingToApprove.isEmpty() && bookingToApprove.get().getStatus().equals(Status.WAITING) &&
                !bookingToApprove.get().getItem().getBookings().isEmpty()) {

            if (bookingToApprove.get().getItem().getUser().getId() != userId) {
                throw new NotFoundException("Пользователь с ИД " + userId + " не найден");
            }

            switch (approved) {
                case "false":
                    bookingToApprove.get().setStatus(Status.REJECTED);
                    iBookingRepository.save(bookingToApprove.get());
                    return true;

                case "true":
                    bookingToApprove.get().setStatus(Status.APPROVED);
                    iBookingRepository.save(bookingToApprove.get());

                    List<Booking> crossingBookings = iBookingRepositoryCustom.getAllItemBookingsWithWaitingStatus(bookingId).stream()
                            .filter(f -> f.getStartDate().isAfter(bookingToApprove.get().getStartDate())
                                    && f.getStartDate().isBefore(bookingToApprove.get().getEndDate()) ||
                                    f.getEndDate().isAfter(bookingToApprove.get().getStartDate())
                                            && f.getEndDate().isBefore(bookingToApprove.get().getEndDate()) ||
                                    f.getStartDate().isBefore(bookingToApprove.get().getStartDate())
                                            && f.getEndDate().isAfter(bookingToApprove.get().getEndDate()))
                            .collect(Collectors.toList());

                    crossingBookings.stream().forEach(e -> e.setStatus(Status.REJECTED));
                    crossingBookings.stream().forEach(e -> iBookingRepository.save(e));

                    return true;

                default:
                    return false;
            }
        }
        return false;
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

    public BookingInfoDto getBookingAfterApproved(int bookingId) {
        Optional<Booking> booking = iBookingRepository.findById(bookingId);

        return BookingMapper.toBookingDto(booking.get());
    }
}
