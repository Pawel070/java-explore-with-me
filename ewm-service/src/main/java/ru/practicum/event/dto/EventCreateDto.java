package ru.practicum.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.practicum.event.service.ValidationInterface;
import ru.practicum.location.dto.LocationDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDto {

    @NotBlank(groups = ValidationInterface.AddEvent.class)
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @NotBlank(groups = ValidationInterface.AddEvent.class)
    @Size(min = 20, max = 5000)
    private String description;

    @NotBlank(groups = ValidationInterface.AddEvent.class)
    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    private int participantLimit;

    private Boolean requestModeration;

    @NotBlank(groups = ValidationInterface.AddEvent.class)
    @Size(min = 3, max = 120)
    private String title;

}