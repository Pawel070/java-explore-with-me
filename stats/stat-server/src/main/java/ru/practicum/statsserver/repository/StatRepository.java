package ru.practicum.statsserver.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsserver.model.ViewStats;


public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT NEW ru.practicum.server.model.ViewStats(e.app, e.uri, COUNT(distinct e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findAllUniqueStatsByDateBetween(@Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end);

    @Query("SELECT NEW ru.practicum.server.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findAllStatsByDateBetween(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);


    @Query("SELECT NEW ru.practicum.server.model.ViewStats(e.app, e.uri, COUNT(distinct e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND e.uri in :uris " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findAllUniqueStatsUriByDateBetween(@Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end,
                                                       @Param("uris") List<String> uris);

    @Query("SELECT NEW ru.practicum.server.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND e.uri in :uris " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findAllStatsUriByDateBetween(@Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end,
                                                 @Param("uris") List<String> uris);
}
