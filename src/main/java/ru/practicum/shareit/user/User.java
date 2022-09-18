package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Indexed
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private Integer id;

    @NotBlank
    @Email(message = "Please enter a valid e-mail address")
    @NonNull
    @Column(name = "USER_EMAIL", nullable = false)
    private String email;

    @Column(name = "USER_NAME", nullable = false)
    @NonNull
    @NotBlank
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Item> items;

}
