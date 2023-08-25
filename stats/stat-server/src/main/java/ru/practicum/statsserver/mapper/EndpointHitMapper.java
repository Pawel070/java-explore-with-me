package ru.practicum.statsserver.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statsserver.model.EndpointHit;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndpointHitMapper {

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        log.info("EndpointHitMapper toEndpointHitDto ...");
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        log.info("EndpointHitMapper toEndpointHit ...");
        return EndpointHit.builder()
                .id(endpointHitDto.getId())
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }
}
