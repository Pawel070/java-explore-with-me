package ru.practicum.comments.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentRequestDto;
import ru.practicum.comments.service.CommentService;


@RestController
@RequestMapping(path = "/admin/comments")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CommentsController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentDto editCommentByAdmin(@PathVariable Long commentId,
                                         @RequestBody @Valid CommentRequestDto updateCommentDto) {
        log.info("Комментарий с УИН {} изменён администратором.", commentId);
        return commentService.editCommentByAdmin(commentId, updateCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        log.info("Комментарий с УИН {} удален администратором.", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }
}
