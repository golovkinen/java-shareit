package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;
import ru.practicum.shareit.annotation.TrueOrFalse;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Indexed
@Table(name = "ITEMS")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID", nullable = false)
    private Integer id;
    @Column(name = "ITEM_NAME", nullable = false)
    @NonNull
    @NotBlank
    @FullTextField
    private String name;
    @Column(name = "ITEM_DESCRIPTION", nullable = false)
    @NonNull
    @NotBlank
    @Size(max = 200)
    @FullTextField
    private String description;
    @Column(name = "ITEM_AVAILABLE")
    @NonNull
    @TrueOrFalse
    @GenericField
    private Boolean available;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @IndexedEmbedded
    private User user;
}
