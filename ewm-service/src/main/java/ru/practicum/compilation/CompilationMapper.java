package ru.practicum.compilation;

import java.util.HashSet;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;

import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.EventMapper;
import ru.practicum.event.model.Event;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(CompilationCreateDto newCompilationDto, HashSet<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.getPinned() == null ? false : newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents().stream().map(EventMapper::toEventModelDto).collect(Collectors.toSet()))
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .build();
    }

    public Compilation toCompilationForUpdate(Compilation compilation, CompilationCreateDto updateCompilationDto, HashSet<Event> events) {
        return Compilation.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(updateCompilationDto.getPinned() == null ? compilation.getPinned() : updateCompilationDto.getPinned())
                .title(updateCompilationDto.getTitle() == null ? compilation.getTitle() : updateCompilationDto.getTitle())
                .build();
    }

}