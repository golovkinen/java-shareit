package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface IRequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByUserIdOrderByCreatedDesc(int userId);

    @Query("select r from Request r where r.user.id <> :userId")
    List<Request> getPagedRequests(
            @Param(value = "userId") int itemId,
            Pageable pageable);
}
