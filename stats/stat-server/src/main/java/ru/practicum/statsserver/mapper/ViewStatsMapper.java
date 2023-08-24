package ru.practicum.statsserver.mapper;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statsserver.model.ViewStats;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ViewStatsMapper {

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        log.info("ViewStatsMapper toViewStatsDto...");
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }

    public static List<ViewStatsDto> listToViewStatsDto(List<ViewStats> list) {
        log.info("ViewStatsMapper listToViewStatsDto...");
        if (!list.isEmpty()) {
            return list.stream()
                    .map(ViewStatsMapper::toViewStatsDto)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
