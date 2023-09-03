package ru.practicum.event;

import java.util.List;

import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventStates;

public interface EventUsageRepository {

    List<Event> findEventsAdmin(List<Long> users, List<EventStates> states, List<Long> categories,
                                String rangeStart, String rangeEnd, int from, int size);

    List<Event> findEventsAll(String text, List<Long> categories, Boolean paid,
                                    String start, String end, int from, int size);

}