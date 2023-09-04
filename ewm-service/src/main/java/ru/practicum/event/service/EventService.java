package ru.practicum.event.service;

import java.util.List;

import ru.practicum.event.model.EventStates;
import ru.practicum.event.dto.*;


public interface EventService {
    EventDto addEvent(EventCreateDto newEventDto, Long id);

    List<EventModelDto> findEvents(Long id, int from, int size);

    EventDto findEvent(Long idUser, Long idEvent);

    EventDto updateEvent(Long idUser, Long idEvent, EventReplRequestDto updateEventUserRequestDto);

    List<EventDto> findEventsAdmin(List<Long> users, List<EventStates> states, List<Long> categories,
                                      String rangeStart, String rangeEnd, int from, int size);

    EventDto updateEventAdmin(Long eventId, EventAReplRequestDto updateEventRequestDto);

    List<EventModelDto> findEventsAll(String text, List<Long> categories, Boolean paid,
                                            String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                            MyConstantsEven sort, int from, int size, String remoteAddress);

    EventDto findEventAll(Long id, String remoteAddress);

}