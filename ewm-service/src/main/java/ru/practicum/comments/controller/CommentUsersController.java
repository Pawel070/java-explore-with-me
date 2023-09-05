package ru.practicum.comments.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentAddDto;
import ru.practicum.comments.dto.CommentRequestDto;
import ru.practicum.comments.service.CommentService;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommentUsersController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentByAuthor(@PathVariable Long userId, @RequestParam Long eventId,
                                            @RequestBody @Valid CommentAddDto commentAddDto) {
        log.info("Пользователь УИН {} прокомментировал событие УИН {}", userId, eventId);
        return commentService.addCommentByAuthor(userId, eventId, commentAddDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto editCommentByAuthor(@PathVariable Long commentId, @PathVariable Long userId,
                                          @RequestBody @Valid CommentRequestDto updateCommentDto) {
        log.info("Комментарий УИН {} отредактирован создателем УИН {}", commentId, userId);
        return commentService.editCommentByAuthor(commentId, userId, updateCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAuthor(@PathVariable Long commentId, @PathVariable Long userId) {
        log.info("Комментарий УИН {} удален создателем УИН {}", commentId, userId);
        commentService.deleteCommentByAuthor(commentId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllCommentsByAuthor(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос всех комментариев пользователя УИН {} ", userId);
        return commentService.getAllCommentsByAuthor(userId, from, size);
    }
}
