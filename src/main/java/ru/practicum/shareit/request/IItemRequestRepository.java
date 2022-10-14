package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
}
