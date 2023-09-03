package ru.practicum.request;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import ru.practicum.event.model.Event;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.ParticipationRequest;

@Mapper(componentModel = "spring", uses = {Event.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
 public interface RequestMapper {

    static ParticipationRequestDto toParticipationRequestDto(Long id, ParticipationRequest participationRequest) {
        return null;
    }

    default ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .created(participationRequest.getCreated())
                .id(participationRequest.getId())
                .status(participationRequest.getStatus())
                .requester(participationRequest.getRequester().getId())
                .event(participationRequest.getEvent().getId())
                .build();
    }

}
/*
@UtilityClass
public class RequestMapper {

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .created(participationRequest.getCreated())
                .id(participationRequest.getId())
                .status(participationRequest.getStatus())
                .requester(participationRequest.getRequester().getId())
                .event(participationRequest.getEvent().getId())
                .build();
    }

}

 */