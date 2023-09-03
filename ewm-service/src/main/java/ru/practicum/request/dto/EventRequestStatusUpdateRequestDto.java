package ru.practicum.request.dto;

import java.util.List;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestStatusUpdateRequestDto {

    private List<Long> requestIds;

    private RequestUpdateStatus status;

}