package ru.practicum.comments.service;

import java.util.List;

import ru.practicum.comments.dto.CommentAddDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentRequestDto;

public interface CommentService {

    CommentDto addCommentByAuthor(Long userId, Long eventId, CommentAddDto commentAddDto);

    CommentDto editCommentByAuthor(Long commentId, Long userId, CommentRequestDto commentRequestDto);

    void deleteCommentByAuthor(Long commentId, Long userId);

    List<CommentDto> getAllCommentsByAuthor(Long userId, Integer from, Integer size);

    CommentDto editCommentByAdmin(Long commentId, CommentRequestDto commentRequestDto);

    void deleteCommentByAdmin(Long commentId);

    List<CommentDto> getAllCommentsForEventId(Long eventId, Integer from, Integer size);

    CommentDto getCommentById(Long commentId);
}
