package ru.practicum.request;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.practicum.request.model.ParticipationRequest;


@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Boolean existsByRequester_IdAndEvent_Id(Long userId, Long eventId);

    List<ParticipationRequest> findByRequester_Id(Long userId);

    List<ParticipationRequest> findByEvent_Id(Long idEvent);

}