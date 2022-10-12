package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", nullable = false)
    private Integer bookingId;
    @Column(name = "start_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    private LocalDateTime startDate;
    @Column(name = "end_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User user;
}
