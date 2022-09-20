package ru.practicum.shareit.item.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IItemRepository extends JpaRepository<Item, Integer> {

    @Query("DELETE FROM Item item WHERE item.user = :id")
    void deleteAllUserItems(@Param(value = "id") int id);


}
