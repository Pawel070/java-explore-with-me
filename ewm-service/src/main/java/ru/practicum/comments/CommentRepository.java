package ru.practicum.comments;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.comments.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);

    List<Comment> findAllByAuthorId(Long userId, Pageable pageable);

}
