package ru.practicum.event.dto;

import lombok.*;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserSmDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventModelDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    private String eventDate;

    private UserSmDto initiator;

    private boolean paid;

    private String title;

    private Long views;

}