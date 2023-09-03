package ru.practicum.request.dto;

import java.time.LocalDateTime;

import lombok.*;

import ru.practicum.request.model.RequestStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {

    private Long id;

    private LocalDateTime created;

    private Long event;

    private Long requester;

    private RequestStatus status;

}