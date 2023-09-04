package ru.practicum.event.dto;

import lombok.*;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.EventStates;
import ru.practicum.location.model.Location;
import ru.practicum.user.dto.UserSmDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto extends EventModelDto {

    private String createdOn;
    private String description;
    private Location location;
    private int participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private EventStates state;

    @Builder
    public EventDto(Long id, String annotation, CategoryDto category, int confirmedRequests, String eventDate,
                    UserSmDto initiator, boolean paid, String title, Long views, String createdOn,
                    String description, Location location, int participantLimit, String publishedOn,
                    boolean requestModeration, EventStates state) {

        super(id, annotation, category, confirmedRequests, eventDate, initiator, paid, title, views);

        this.createdOn = createdOn;
        this.description = description;
        this.location = location;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
    }

}