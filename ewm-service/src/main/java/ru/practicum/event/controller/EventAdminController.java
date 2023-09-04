package ru.practicum.event.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.event.dto.EventAReplRequestDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.model.EventStates;
import ru.practicum.event.service.EventService;

@RestController
@Slf4j
@Validated
@RequestMapping("/admin")
public class EventAdminController {
    private final EventService eventService;

    @Autowired
    public EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public List<EventDto> findEventsAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventStates> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        List<EventDto> resulting = eventService.findEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("EventAdminController findEventsAdmin: Cписок событий запрошен {}.", resulting);
        return resulting;
    }

    @PatchMapping("/events/{eventId}")
    public EventDto updateEventAdmin(@PathVariable Long eventId,
                                            @RequestBody @Valid EventAReplRequestDto updateEventRequestDto) {
        EventDto resulting = eventService.updateEventAdmin(eventId, updateEventRequestDto);
        log.info("EventAdminController updateEventAdmin: Изменение события {}.", resulting);
        return resulting;
    }

}