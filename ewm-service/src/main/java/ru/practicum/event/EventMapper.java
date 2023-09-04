package ru.practicum.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.experimental.UtilityClass;

import ru.practicum.MyConstants;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventStates;
import ru.practicum.location.LocationMapper;
import ru.practicum.user.UserMapper;
import ru.practicum.user.dto.UserSmDto;

@UtilityClass
public class EventMapper {

    public Event toEvent(EventCreateDto eventCreateDto) {
        return Event.builder()
                .annotation(eventCreateDto.getAnnotation())
                .eventDate(LocalDateTime.parse(eventCreateDto.getEventDate(),DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .createdOn(LocalDateTime.now())
                .confirmedRequests(0)
                .description(eventCreateDto.getDescription())
                .category(Category.builder().id(eventCreateDto.getCategory()).build())
                .location(LocationMapper.toLocation(eventCreateDto.getLocation()))
                .paid(eventCreateDto.getPaid() == null ? false : eventCreateDto.getPaid())
                .participantLimit(eventCreateDto.getParticipantLimit())
                .state(EventStates.PENDING)
                .requestModeration(eventCreateDto.getRequestModeration())
                .title(eventCreateDto.getTitle())
                .build();
    }

    public EventDto toEventDtoForSave(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .build())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate()
                        .format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .initiator(UserSmDto.builder()
                        .id(event.getInitiator().getId()).build())
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .createdOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .description(event.getDescription())
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .build();
    }

    public EventModelDto toEventModelDto(Event event) {
        return EventDto.builder()
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(UserMapper.toUserSmDto(event.getInitiator()))
                .id(event.getId())
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
     public EventDto toEventDtoForUser(Event event) {
         return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .initiator(UserMapper.toUserSmDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .createdOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .description(event.getDescription())
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn()
                        .format(DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .build();
    }

    public Event toReplEvent(EventReplRequestDto EventReplRequestDto, Event event) {
        return Event.builder()
                .id(event.getId())
                .annotation(EventReplRequestDto.getAnnotation() != null ? EventReplRequestDto.getAnnotation() : event.getAnnotation())
                .category(EventReplRequestDto.getCategory() != null ? Category.builder()
                        .id(EventReplRequestDto.getCategory()).build() : event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(EventReplRequestDto.getDescription() != null ? EventReplRequestDto.getDescription() : event.getDescription())
                .eventDate(EventReplRequestDto.getEventDate() != null ? LocalDateTime.parse(EventReplRequestDto.getEventDate(),
                        DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)) : event.getEventDate())
                .initiator(event.getInitiator())
                .location(EventReplRequestDto.getLocation() != null ? LocationMapper.toLocation(EventReplRequestDto.getLocation()) : event.getLocation())
                .paid(EventReplRequestDto.getPaid() != null ? EventReplRequestDto.getPaid() : event.isPaid())
                .participantLimit(EventReplRequestDto.getParticipantLimit() != 0 ? EventReplRequestDto.getParticipantLimit() : event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .requestModeration(EventReplRequestDto.getRequestModeration() == null ? event.getRequestModeration() : EventReplRequestDto.getRequestModeration())
                .title(EventReplRequestDto.getTitle() != null ? EventReplRequestDto.getTitle() : event.getTitle())
                .views(event.getViews())
                .build();

    }

    public Event toAReplEvent(EventAReplRequestDto eventAReplRequestDto, Event event) {
        return Event.builder()
                .id(event.getId())
                .annotation(eventAReplRequestDto.getAnnotation() != null ? eventAReplRequestDto.getAnnotation() : event.getAnnotation())
                .category(eventAReplRequestDto.getCategory() != null ? Category.builder()
                        .id(eventAReplRequestDto.getCategory()).build() : event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(eventAReplRequestDto.getDescription() != null ? eventAReplRequestDto.getDescription() : event.getDescription())
                .eventDate(eventAReplRequestDto.getEventDate() != null ? LocalDateTime.parse(eventAReplRequestDto.getEventDate(),
                        DateTimeFormatter.ofPattern(MyConstants.DATE_PATTERN)) : event.getEventDate())
                .initiator(event.getInitiator())
                .location(eventAReplRequestDto.getLocation() == null ? event.getLocation() : LocationMapper.toLocation(eventAReplRequestDto.getLocation()))
                .paid(eventAReplRequestDto.getPaid() == null ? event.isPaid() : eventAReplRequestDto.getPaid())
                .participantLimit(eventAReplRequestDto.getParticipantLimit() != 0 ? eventAReplRequestDto.getParticipantLimit() : event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .requestModeration(eventAReplRequestDto.getRequestModeration() == null ? event.getRequestModeration() : eventAReplRequestDto.getRequestModeration())
                .title(eventAReplRequestDto.getTitle() != null ? eventAReplRequestDto.getTitle() : event.getTitle())
                .views(event.getViews())
                .build();
    }

}