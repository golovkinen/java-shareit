package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<Item> items;

    @OneToMany(mappedBy = "user")
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "user")
    private Set<Request> requests;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;
}
