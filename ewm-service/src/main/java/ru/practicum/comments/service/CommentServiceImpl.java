package ru.practicum.comments.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.CommentMapper;
import ru.practicum.comments.CommentRepository;
import ru.practicum.comments.dto.CommentAddDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentRequestDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventStates;
import ru.practicum.exceptions.CommentConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentDto addCommentByAuthor(Long userId, Long eventId, CommentAddDto commentAddDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id " + userId + " doesn't exist"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " doesn't exist"));

        if (!event.getState().equals(EventStates.PUBLISHED)) {
            throw new CommentConflictException("You can't add a comment to an unpublished event");
        }

        Comment comment = CommentMapper.toComment(commentAddDto, user, event);
        Comment newComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(newComment);
    }

    @Override
    @Transactional
    public CommentDto editCommentByAuthor(Long commentId, Long userId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId);
        if (comment == null) {
            throw new CommentConflictException("Comment with id " + commentId + " created by user with id " + userId + " doesn't exist");
        }

        comment.setText(commentRequestDto.getText());
        Comment updComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(updComment);
    }

    @Override
    @Transactional
    public void deleteCommentByAuthor(Long commentId, Long userId) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId);
        if (comment == null) {
            throw new NotFoundException("Comment with id " + commentId + " created by user with id " + userId + " doesn't exist");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getAllCommentsByAuthor(Long userId, Integer from, Integer size) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id " + userId + " doesn't exist"));

        Sort sort = Sort.by(Sort.Direction.DESC, "createdOn");
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        List<Comment> authorComments = commentRepository.findAllByAuthorId(userId, pageRequest);
        return authorComments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto editCommentByAdmin(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("User with id " + commentId + " doesn't exist"));
        comment.setText(commentRequestDto.getText());
        Comment updComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(updComment);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("User with id " + commentId + " doesn't exist"));
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getAllCommentsForEventId(Long eventId, Integer from, Integer size) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " doesn't exist"));
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Comment> comments = commentRepository.findAllByEventId(eventId, pageRequest);
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("User with id " + commentId + " doesn't exist"));
        return CommentMapper.toCommentDto(comment);
    }
}

