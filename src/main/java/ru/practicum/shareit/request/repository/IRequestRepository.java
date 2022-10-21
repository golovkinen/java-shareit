package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface IRequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByUserIdOrderByCreatedDesc(int userId);
}
