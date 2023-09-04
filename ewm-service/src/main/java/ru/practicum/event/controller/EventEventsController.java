package ru.practicum.event.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventModelDto;
import ru.practicum.event.service.EventService;
import ru.practicum.event.service.MyConstantsEven;

@RestController
@Slf4j
@Validated
@Transactional(readOnly = true)
@RequestMapping("/events")
public class EventEventsController {
    private final EventService eventService;

    @Autowired
    public EventEventsController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventModelDto> findEventsAll(@RequestParam(required = false) String text,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false) Boolean paid,
                                                   @RequestParam(required = false) String rangeStart,
                                                   @RequestParam(required = false) String rangeEnd,
                                                   @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                   @RequestParam(required = false) MyConstantsEven sort,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size,
                                                   HttpServletRequest request) {
        List<EventModelDto> resulting = eventService.findEventsAll(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request.getRemoteAddr());
        log.info("EventEventsController findEventsAll: Запрос списка событий {} .", resulting);
        return resulting;
    }

    @GetMapping("/{id}")
    public EventDto findEventAll(@PathVariable Long id, HttpServletRequest request) {
        EventDto resulting = eventService.findEventAll(id, request.getRemoteAddr());
        log.info("EventEventsController findEventAll: Получение cобытия {} .", resulting);
        return resulting;
    }

}