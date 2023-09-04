package ru.practicum.compilation.dto;

import java.util.Set;

import lombok.*;

import ru.practicum.event.dto.EventModelDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {

    private Long id;

    private Set<EventModelDto> events;

    private Boolean pinned;

    private String title;

}