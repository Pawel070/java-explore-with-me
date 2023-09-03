package ru.practicum.event;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.LocalDateTime;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.criteria.Predicate;

import lombok.RequiredArgsConstructor;
import lombok.var;

import org.springframework.stereotype.Repository;

import ru.practicum.MyConstants;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventStates;

@Repository
@RequiredArgsConstructor
public class EventUsageRepositoryImpl {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private final EntityManager entityManager;

    public List<Event> findEventsAdmin(List<Long> users, List<EventStates> states, List<Long> categories,
                                       String rangeStart, String rangeEnd, int from, int size) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) start = LocalDateTime.parse(rangeStart, ofPattern(MyConstants.DATE_PATTERN));
        if (rangeEnd != null) end = LocalDateTime.parse(rangeEnd, ofPattern(MyConstants.DATE_PATTERN));
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Event.class);
        var root = query.from(Event.class);
        var critery = builder.conjunction();
        if (rangeStart != null) {
            critery = builder.and(critery, builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start));
        }
        if (rangeEnd != null) {
            critery = builder.and(critery, builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end));
        }
        if (categories != null && categories.size() > 0) {
            critery = builder.and(critery, root.get("category").in(categories));
        }
        if (users != null && users.size() > 0) {
            critery = builder.and(critery, root.get("initiator").in(users));
        }
        if (states != null) {
            critery = builder.and(critery, root.get("state").in(states));
        }
        query.select(root).where(critery);
        var events = entityManager.createQuery(query).setFirstResult(from).setMaxResults(size).getResultList();
        return events;
    }

        public List<Event> findEventsAll(String text, List<Long> categories, Boolean paid,
                                           String rangeStart, String rangeEnd, int from, int size) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) start = LocalDateTime.parse(rangeStart, ofPattern(MyConstants.DATE_PATTERN));
        if (rangeEnd != null) end = LocalDateTime.parse(rangeEnd, ofPattern(MyConstants.DATE_PATTERN));
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Event.class);
        var root = query.from(Event.class);
        var critery = builder.conjunction();
        if (text != null) {
            critery = builder.and(critery, builder.or(
                    builder.like(builder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                    builder.like(builder.lower(root.get("description")), "%" + text.toLowerCase() + "%")));
        }
        if (categories != null && categories.size() > 0) {
            critery = builder.and(critery, root.get("category").in(categories));
        }
        if (paid != null) {
            Predicate predicate;
            if (paid) predicate = builder.isTrue(root.get("paid"));
            else predicate = builder.isFalse(root.get("paid"));
            critery = builder.and(critery, predicate);
        }
        if (end != null) {
            critery = builder.and(critery, builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end));
        }
        if (start != null) {
            critery = builder.and(critery, builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start));
        }
        query.select(root).where(critery).orderBy(builder.asc(root.get("eventDate")));
        List<Event> events = entityManager.createQuery(query).setFirstResult(from).setMaxResults(size).getResultList();
        return events;
    }

}