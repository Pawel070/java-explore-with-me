package ru.practicum.comments.controller;


import java.util.List;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.CommentService;


@RestController
@RequestMapping(path = "/comments")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommentAllController {

    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllCommentsForEventId(@RequestParam Long eventId,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос всех комментариев по событию УИН {}", eventId);
        return commentService.getAllCommentsForEventId(eventId, from, size);
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentById(@PathVariable Long commentId) {
        log.info("Запрос комментария с УИН {}", commentId);
        return commentService.getCommentById(commentId);
    }
}
