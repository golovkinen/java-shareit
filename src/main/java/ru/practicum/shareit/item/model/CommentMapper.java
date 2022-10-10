package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toComment(CommentCreateDto commentDto, User author, Item item) {
        return new Comment(null, commentDto.getText(), LocalDateTime.now(),
                item, author);
    }

    public static CommentInfoDto toCommentDto(Comment comment) {
        return new CommentInfoDto(comment.getId(), comment.getText(), comment.getUser().getName(),
                comment.getCreated());
    }
}
