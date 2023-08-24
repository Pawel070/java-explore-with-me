package ru.practicum.statsserver.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statsserver.mapper.EndpointHitMapper;
import ru.practicum.statsserver.mapper.ViewStatsMapper;
import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsserver.repository.StatRepository;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    public EndpointHitDto addEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        EndpointHitDto endpointHitDtoResult = EndpointHitMapper.toEndpointHitDto(statRepository.save(endpointHit));
        log.info("Endpoint hit saved: {}", endpointHit);
        return endpointHitDtoResult;
    }

    @Override
    public List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean exclus) {
        if (uris == null || uris.isEmpty()) {
            if (exclus) {
                return ViewStatsMapper.listToViewStatsDto(statRepository.findAllUniqueStatsByDateBetween(start, end));
            } else {
                return ViewStatsMapper.listToViewStatsDto(statRepository.findAllStatsByDateBetween(start, end));
            }
        } else {
            if (exclus) {
                return ViewStatsMapper.listToViewStatsDto(statRepository.findAllUniqueStatsUriByDateBetween(start, end, uris));
            } else {
                return ViewStatsMapper.listToViewStatsDto(statRepository.findAllStatsUriByDateBetween(start, end, uris));
            }
        }
    }
}
