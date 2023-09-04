package ru.practicum.event.service;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.MyConstants;
import ru.practicum.category.CategoryMapper;
import ru.practicum.event.EventMapper;
import ru.practicum.event.EventRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventStates;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.NotValidException;
import ru.practicum.exceptions.WrongEventDateException;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationRepository;
import ru.practicum.location.model.Location;
import ru.practicum.statclient.StatClient;
import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final StatClient statClient;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public EventDto addEvent(EventCreateDto newEventDto, Long id) {
        log.info("EventServiceImpl addEvent:  Создание События {}", newEventDto);
        if (newEventDto.getEventDate() != null && !LocalDateTime
                .parse(newEventDto.getEventDate(), ofPattern(MyConstants.DATE_PATTERN))
                .isAfter(LocalDateTime.now())) {
            throw new NotValidException("Ошибка точки начала.");
        }
        if (LocalDateTime.parse(newEventDto.getEventDate(), ofPattern(MyConstants.DATE_PATTERN))
                .isBefore(LocalDateTime.now().plusHours(2))) {
            throw new WrongEventDateException("Точка начала события не раньше 2х часов спустя.");
        }
        if (newEventDto.getRequestModeration() == null) newEventDto.setRequestModeration(true);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Ошибка УИН пользователя."));
        Event event = EventMapper.toEvent(newEventDto);
        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        log.info("EventServiceImpl addEvent: Пользователь --> {}, Событие --> {}, Место --> {}", user, event, location);
        event.setInitiator(user);
        event.setLocation(location);
        EventDto resulting = EventMapper.toEventDtoForSave(eventRepository.save(event));
        resulting.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        log.info("EventServiceImpl addEvent: Cоздано событие --> {} ", resulting);
        return resulting;
    }

    @Override
    @Transactional
    public List<EventModelDto> findEvents(Long id, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        log.info("EventServiceImpl findEvents: Получение списка событий для пользователя УИН {}", id);
        return eventRepository.findEventByInitiatorId(id, pageable)
                .getContent().stream().map(EventMapper::toEventModelDto).collect(toList());
    }

    @Override
    @Transactional
    public EventDto findEvent(Long idUser, Long idEvent) {
        Event event = eventRepository.findEventByIdAndInitiatorId(idEvent, idUser);
        EventDto resulting = eventMapper.toEventDtoForUser(event);
        log.info("EventServiceImpl findEvent: Получение события для пользователя УИН {} --> {}", idUser, resulting);
        return resulting;
    }

    @Override
    @Transactional
    public EventDto updateEvent(Long idUser, Long idEvent, EventReplRequestDto updateEventUserRequestDto) {
        Event event = eventRepository.findEventByIdAndInitiatorId(idEvent, idUser);
        log.info("EventServiceImpl updateEvent: Изменение события --> {}", event);
        if (event.getPublishedOn() != null) {
            throw new DataIntegrityViolationException("Событие было опубликованно ранее.");
        }
        if (updateEventUserRequestDto.getEventDate() != null && !LocalDateTime
                .parse(updateEventUserRequestDto.getEventDate(), ofPattern(MyConstants.DATE_PATTERN))
                .isAfter(LocalDateTime.now())) {
            throw new NotValidException("Ошибка точки начала.");
        }
        if (updateEventUserRequestDto.getLocation() != null) {
            updateEventUserRequestDto.setLocation(LocationMapper.toLocationDto(locationRepository.save(LocationMapper
                    .toLocation(updateEventUserRequestDto.getLocation()))));
        }
        if (updateEventUserRequestDto.getStateAction() != null) {
            if (updateEventUserRequestDto.getStateAction().equals(MyConstantsEven.SEND_TO_REVIEW)) {
                event.setState(EventStates.PENDING);
            } else if (updateEventUserRequestDto.getStateAction().equals(MyConstantsEven.CANCEL_REVIEW)) {
                event.setState(EventStates.CANCELED);
            }
        }
        Event updatedEvent = eventMapper.toReplEvent(updateEventUserRequestDto, event);
        if (updatedEvent.getLocation() == null) {
            updatedEvent.setLocation(locationRepository.save(LocationMapper.toLocation(updateEventUserRequestDto.getLocation())));
        }
        log.info("EventServiceImpl updateEvent: Событие изменено на --> {}", updatedEvent);
        return eventMapper.toEventDtoForUser(eventRepository.save(updatedEvent));
    }

    @Override
    @Transactional
    public List<EventDto> findEventsAdmin(List<Long> users, List<EventStates> states, List<Long> categories,
                                          String rangeStart, String rangeEnd, int from, int size) {
        log.info("EventServiceImpl findEventsAdmin: Получение спика событий.");
        List<Event> events = eventRepository.findEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        if (events.size() == 0) return new ArrayList<>();
        return events.stream().map(EventMapper::toEventDtoForUser).collect(toList());
    }

    @Override
    @Transactional
    public EventDto updateEventAdmin(Long eventId, EventAReplRequestDto updateEventRequestDto) {
        log.info("EventServiceImpl updateEventAdmin: Изменение события.");
        if (updateEventRequestDto.getEventDate() != null && LocalDateTime.parse(updateEventRequestDto.getEventDate(),
                        ofPattern(MyConstants.DATE_PATTERN)).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new NotValidException("Точка начала события не раньше 2х часов спустя.");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет указанного события."));
        if (event.getState().equals(EventStates.PUBLISHED) && updateEventRequestDto.getStateAction()
                .equals(MyConstantsEven.PUBLISH_EVENT)) throw new DataIntegrityViolationException("Событие было опубликовано ранее.");
        if (event.getState().equals(EventStates.CANCELED) &&
                updateEventRequestDto.getStateAction().equals(MyConstantsEven.PUBLISH_EVENT)) {
            throw new DataIntegrityViolationException("Событие уже отменено.");
        } else if (event.getState().equals(EventStates.PUBLISHED) &&
                updateEventRequestDto.getStateAction().equals(MyConstantsEven.REJECT_EVENT)) {
            throw new DataIntegrityViolationException("Событие было опубликовано, отменить нельзя.");
        }
        if (updateEventRequestDto.getStateAction() == null) {
            event.setState(event.getState());
        } else if (updateEventRequestDto.getStateAction().equals(MyConstantsEven.PUBLISH_EVENT)) {
            event.setState(EventStates.PUBLISHED);
        } else if (updateEventRequestDto.getStateAction().equals(MyConstantsEven.REJECT_EVENT)) {
            event.setState(EventStates.CANCELED);
        }
        Event upEvent = eventMapper.toAReplEvent(updateEventRequestDto, event);
        if (upEvent.getState().equals(EventStates.PUBLISHED)) upEvent.setPublishedOn(LocalDateTime.now());
        EventDto resulting = eventMapper.toEventDtoForUser(eventRepository.save(upEvent));
        log.info("EventServiceImpl updateEventAdmin: Событие измененно на --> {} ", resulting);
        return resulting;
    }

    @Override
    public List<EventModelDto> findEventsAll(String text, List<Long> categories, Boolean paid,
                                             String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                             MyConstantsEven sort, int from, int size, String remoteAddress) {
        log.info("EventServiceImpl findEventsAll: Получение спика событий.");
        if (rangeStart != null && rangeEnd != null) {
            if (LocalDateTime.parse(rangeStart, ofPattern(MyConstants.DATE_PATTERN))
                    .isAfter(LocalDateTime.parse(rangeEnd, ofPattern(MyConstants.DATE_PATTERN)))) {
                throw new NotValidException("Временные точкина заданы не верно");
            }
        }
        List<Event> events = eventRepository.findEventsAll(text, categories, paid, rangeStart, rangeEnd, from, size);
        if (onlyAvailable) {
            events = events.stream().filter((event -> event.getConfirmedRequests() < (long) event.getParticipantLimit()))
                    .collect(toList());
        }
        if (sort != null) {
            if (sort.equals(MyConstantsEven.EVENT_DATE)) {
                events = events.stream().sorted(comparing(Event::getEventDate)).collect(toList());
            } else events = events.stream().sorted(comparing(Event::getViews)).collect(toList());
        }
        if (events.size() == 0) return new ArrayList<>();
        addHitsForEvents(events, remoteAddress);
        events = addViews(events);
        log.info("EventServiceImpl findEventsAll: Спиcок событий получен --> {} ", events);
        return events.stream().map(EventMapper::toEventModelDto).collect(toList());
    }

    @Transactional
    @Override
    public EventDto findEventAll(Long id, String remoteAddress) {
        log.info("EventServiceImpl findEventAll: Получение события.");
        LocalDateTime time = LocalDateTime.now();
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(""));
        if (!event.getState().equals(EventStates.PUBLISHED)) {
            throw new NotFoundException("Указанное событие отсутствует в базе.");
        }
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("main-service")
                .uri("/events" + "/" + id)
                .ip(remoteAddress)
                .timestamp(LocalDateTime.parse(time.format(ofPattern(MyConstants.DATE_PATTERN)))).build();
        statClient.addStat(endpointHitDto);
         eventRepository.save(event);
        log.info("EventServiceImpl findEventAll: Событие полученно --> {} ", event);
        return eventMapper.toEventDtoForUser(event);
    }

    private void addHitsForEvents(List<Event> events, String remoteAddress) {
        log.info("EventServiceImpl addHitsForEvents: Добавление события к Hit.");
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("main-service")
                .uri("/events")
                .ip(remoteAddress)
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(ofPattern(MyConstants.DATE_PATTERN)))).build();
        statClient.addStat(endpointHitDto);
    }

    private List<Event> addViews(List<Event> events) {
        log.info("EventServiceImpl addViews: Добавление количества просмотров к событиям.");
        String startDate = LocalDateTime.now().minusMonths(1).format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN));
        String endDate = LocalDateTime.now().plusMonths(1).format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN));
        String eventsUri = "/events/";
        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
        List<String> uris = eventIds.stream().map(id -> eventsUri + id).collect(Collectors.toList());
        ResponseEntity<Object> response = statClient.getStat(startDate, endDate, uris, true);
        if (response.hasBody()) {
            List<HashMap<String, Object>> body = (List<HashMap<String, Object>>) response.getBody();
            HashMap<Long, Long> map = new HashMap<>();
            for (int index = 0; index < body.size(); index++) {
                String bufer = (String) body.get(index).get("uri");
                String buferDop = bufer.replace("/events/", "");
                Long resilting = Long.valueOf(buferDop);
                map.put(resilting, Long.valueOf(String.valueOf(body.get(index).get("hits"))));
            }
            for (Event event : events) event.setViews(map.get(event.getId()));
        }
        return events;
    }
}