package ru.practicum.comments;

import java.time.LocalDateTime;

import lombok.experimental.UtilityClass;

import ru.practicum.comments.dto.CommentAddDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.user.UserMapper;
import ru.practicum.user.model.User;

@UtilityClass
public final class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.toUserSmDto(comment.getAuthor()))
                .event(EventMapper.toEventModelDto(comment.getEvent()))
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public static Comment toComment(CommentAddDto newCommentDto, User author, Event event) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .author(author)
                .event(event)
                .createdOn(LocalDateTime.now())
                .build();
    }

}