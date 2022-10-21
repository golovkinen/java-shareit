package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
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
@Indexed
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    @GenericField(sortable = Sortable.YES)
    private Integer id;

    @Column(name = "item_name", nullable = false)
    @FullTextField
    private String name;

    @Column(name = "item_description", nullable = false)
    @FullTextField
    private String description;

    @Column(name = "item_available", nullable = false)
    @GenericField
    private Boolean available;

    @ManyToOne
    @IndexedEmbedded
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
