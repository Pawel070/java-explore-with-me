package ru.practicum.request.service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventStates;
import ru.practicum.event.EventRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestUpdateStatus;
import ru.practicum.request.RequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.UserRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId, LocalDateTime time) {
        Event event = eventRepository.lockById(eventId);
        log.info("RequestServiceImpl createRequest: Создание заявки для пользователя УИН {} на событие --> {}.", userId, event);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким УИН нет в базе."));
        if (requestRepository.existsByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new DataIntegrityViolationException("Пользователь уже подал заявку на это событие.");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new DataIntegrityViolationException("Автор события не может подать на него заявку.");
        }
        if (event.getState().equals(EventStates.PENDING)) {
            throw new DataIntegrityViolationException("Указанное событие не опубликовано.");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new DataIntegrityViolationException("Заявки пока не принимаются в связи в превышением лимита.");
        }
        RequestStatus status = null;
        if (event.getRequestModeration()) status = RequestStatus.PENDING;
        if (event.getRequestModeration() == null || !event.getRequestModeration() || event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(ParticipationRequest.builder()
                .created(time)
                .event(event)
                .requester(user)
                .status(status)
                .build()));
    }

    @Override
    @Transactional
    public ParticipationRequestDto canceledRequest(Long userId, Long requestId) {
        log.info("RequestServiceImpl canceledRequest: Отмена заявки УИН {} пользователя УИН {}", requestId, userId);;
        ParticipationRequest resulting = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Указанный запрос заблокирован или не существует."));
        resulting.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(resulting));
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> findRequests(Long userId) {
        log.info("RequestServiceImpl findRequests: Получение заявок пользователя УИН {}", userId);
        if (!userRepository.existsById(userId)) throw new NotFoundException("Пользователь с УИН {} отсутствует в базе.");
        return requestRepository.findByRequester_Id(userId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> findRequestsByUsersEvent(Long eventId, Long userId) {
        log.info("RequestServiceImpl findRequestsByUsersEvent: Поиск заявок пользователей на событие {}", eventId);
        if (!userRepository.existsById(userId)) throw new NotFoundException("Пользователь с УИН {} отсутствует в базе.");
        if (!eventRepository.existsById(eventId)) throw new NotFoundException("Такого события нет в базе.");
        return requestRepository.findByEvent_Id(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDto updateStatusRequests(Long userId, Long eventId,
                                                                  EventRequestStatusUpdateRequestDto statusUpdateRequest) {
        Event event = eventRepository.lockById(eventId);
        log.info("RequestServiceImpl updateStatusRequests: Изменение статуса заявок пользователя УИК {} на событие -->", userId, event);
        if (!userRepository.existsById(userId)) throw new NotFoundException("Пользователь отсутствует в базе.");
        List<ParticipationRequest> confirmedRequestsAnsys = new ArrayList<>();
        List<ParticipationRequest> rejectedRequestsAnsys = new ArrayList<>();
        List<ParticipationRequest> confirmedRequestsUpper= new ArrayList<>();
        List<ParticipationRequest> rejectedRequestsUpper= new ArrayList<>();
        List<ParticipationRequest> requestsChange = requestRepository.findAllById(statusUpdateRequest.getRequestIds());
        if (statusUpdateRequest.getStatus().equals(RequestUpdateStatus.REJECTED)) {
            for (ParticipationRequest request : requestsChange) {
                if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                    throw new DataIntegrityViolationException("Одобренная заявка отмене не подлежит.");
                }
                rejectedRequestsAnsys.add(request);
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequestsUpper.add(request);
            }
        } else {
            if (event.getRequestModeration() == null || event.getRequestModeration()
                    .equals(false) || event.getParticipantLimit().equals(0)) {
                return EventRequestStatusUpdateResultDto.builder()
                        .confirmedRequests(requestRepository.findAllById(statusUpdateRequest.getRequestIds())
                        .stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList()))
                        .rejectedRequests(new ArrayList<>()).build();
            }
            if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
                throw new DataIntegrityViolationException("Заявки пока не принимаются в связи в превышением лимита.");
            }
            for (ParticipationRequest request : requestsChange) {
                if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                    confirmedRequestsAnsys.add(request);
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequestsUpper.add(request);
                } else {
                    if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                        throw new DataIntegrityViolationException("Одобренная заявка отмене не подлежит.");
                    }
                    rejectedRequestsAnsys.add(request);
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedRequestsUpper.add(request);
                }
            }
        }
        int plus = event.getConfirmedRequests();
        event.setConfirmedRequests(plus + confirmedRequestsUpper.size());

        return EventRequestStatusUpdateResultDto.builder()
                .confirmedRequests(confirmedRequestsAnsys.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList()))
                .rejectedRequests(rejectedRequestsAnsys.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList())).build();
    }

}