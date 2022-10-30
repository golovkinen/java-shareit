package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Integer id;

    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "item_description", nullable = false)
    private String description;

    @Column(name = "item_available", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "item")
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "item")
    private Set<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
}
