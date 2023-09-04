package ru.practicum.request.controller;

import java.time.LocalDateTime;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable("userId") Long userId,
                                                 @RequestParam("eventId") Long eventId) {
        ParticipationRequestDto resulting = requestService.createRequest(userId, eventId, LocalDateTime.now());
        log.info("RequestController createRequest: Пользователь УИН {} подал запрос на участие в событии --> {}.", userId, resulting);
        return resulting;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto canceledRequest(@PathVariable("userId") Long userId,
                                                   @PathVariable("requestId") Long requestId) {
        ParticipationRequestDto resulting = requestService.canceledRequest(userId, requestId);
        log.info("RequestController canceledRequest: Пользователь УИН {}, отменил заявку на участие в событии --> {}.", userId, resulting);
        return resulting;
    }

    @GetMapping
    public List<ParticipationRequestDto> findRequests(@PathVariable("userId") Long userId) {
        List<ParticipationRequestDto> resulting = requestService.findRequests(userId);
        log.info("RequestController findRequests: Список запросов пользователя УИН {} --> {} ", userId, resulting);
        return resulting;
    }

}