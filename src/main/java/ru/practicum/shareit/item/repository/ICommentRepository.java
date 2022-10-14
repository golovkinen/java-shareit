package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

public interface ICommentRepository extends JpaRepository<Comment, Integer> {
}
