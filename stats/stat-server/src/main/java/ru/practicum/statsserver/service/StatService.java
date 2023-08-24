package ru.practicum.statsserver.service;

import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStatsDto;


public interface StatService {

    EndpointHitDto addEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
