package ru.practicum.event.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventModelDto;
import ru.practicum.event.dto.EventReplRequestDto;
import ru.practicum.event.service.EventService;
import ru.practicum.event.service.ValidationInterface;
import ru.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

@RestController
@Slf4j
@Validated
@RequestMapping("/users")
public class EventUserController {
    private final EventService eventService;
    private final RequestService requestService;

    @Autowired
    public EventUserController(EventService eventService, RequestService requestService) {
        this.eventService = eventService;
        this.requestService = requestService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Validated(ValidationInterface.AddEvent.class)
    @PostMapping("/{userId}/events")
    public EventDto addEvent(@Valid @RequestBody EventCreateDto newEventDto,
                             @PathVariable(name = "userId") Long id) {
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        EventDto resulting = eventService.addEvent(newEventDto, id);
        log.info("EventUserController addEvent: Пользователь  УИН {} создал новое событие --> {}", id, resulting);
        return resulting;
    }

    @GetMapping("/{userId}/events")
    public List<EventModelDto> findEvents(@PathVariable(name = "userId") Long id,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(defaultValue = "10") @Positive int size) {
        List<EventModelDto> resulting = eventService.findEvents(id, from, size);
        log.info("EventUserController findEvents: Список событий пользователя --> {}", resulting);
        return resulting;
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventDto findEvent(@PathVariable(name = "userId") Long idUser,
                                  @PathVariable(name = "eventId") Long idEvent) {
        EventDto resulting = eventService.findEvent(idUser, idEvent);
        log.info("EventUserController findEvent: Данные о событии --> {}", resulting);
        return resulting;
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventDto updateEvent(@PathVariable(name = "userId") Long idUser,
                                    @PathVariable(name = "eventId") Long idEven,
                                    @RequestBody @Valid EventReplRequestDto updateEventUserRequestDto) {
        EventDto resulting = eventService.updateEvent(idUser, idEven, updateEventUserRequestDto);
        log.info("EventUserController updateEvent: Пользователь УИН {} изменил событие --> {}", idUser, resulting);
        return resulting;
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsByUsersEvent(@PathVariable(name = "userId") Long idUser,
                                                                  @PathVariable(name = "eventId") Long idEvent) {
        List<ParticipationRequestDto> resulting = requestService.findRequestsByUsersEvent(idEvent, idUser);
        log.info("EventUserController findRequestsByUsersEvent: Cписок запросов пользователя {} по событию --> {}", idUser, resulting);
        return resulting;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateStatusRequests(@PathVariable(name = "userId") Long idUser,
                                                                  @PathVariable(name = "eventId") Long idEvent,
                                                                  @RequestBody EventRequestStatusUpdateRequestDto statusUpdateRequest) {
        EventRequestStatusUpdateResultDto resulting = requestService.updateStatusRequests(idUser, idEvent, statusUpdateRequest);
        log.info("EventUserController updateStatusRequests: Изменение статуса заявок пользователем по событию --> {}.", idUser, resulting);
        return resulting;
    }

}