package ru.practicum.compilation.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.compilation.CompilationMapper;
import ru.practicum.compilation.CompilationRepository;
import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.NotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Override
    @Transactional
    public CompilationDto addCompilation(CompilationCreateDto newCompilationDto) {
        var events = newCompilationDto.getEvents() == null ?
                new HashSet<Event>() : eventRepository.findAllById(newCompilationDto.getEvents());
        HashSet<Event> added = new HashSet<>();
        added.addAll(events);
        log.info("CompilationServiceImpl addCompilation: Добавлены события --> {}", added);
        return compilationMapper
                .toCompilationDto(compilationRepository.save(compilationMapper
                        .toCompilation(newCompilationDto, added)));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
        log.info("CompilationServiceImpl deleteCompilation: Подборка с УИН {} удалён.", compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, CompilationCreateDto updateCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки событий с УИН " + compId + "нет в базе."));
        var events = updateCompilationDto.getEvents() == null ?
                new HashSet<Event>() : eventRepository.findAllById(updateCompilationDto.getEvents());
        HashSet<Event> up = new HashSet<>();
        up.addAll(events);
        log.info("CompilationServiceImpl updateCompilation: Добавлены события --> {}", up);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilationMapper
                .toCompilationForUpdate(compilation, updateCompilationDto, up)));
    }

    @Override
    public List<CompilationDto> findCompilations(boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        log.info("CompilationServiceImpl findCompilations: Подборкки запрошены.");
        return compilationRepository.findAllByPinned(pinned, pageable)
                .stream().map(compilation -> compilationMapper.toCompilationDto(compilation)).collect(Collectors.toList());
    }

    @Override
    public CompilationDto findCompilation(Long compId) {
        log.info("CompilationServiceImpl findCompilation: Запрошена подборка событииз с УИН {} .", compId);
        return compilationMapper.toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка событий с УИН " + compId + "не найдена в базе.")));
    }

}