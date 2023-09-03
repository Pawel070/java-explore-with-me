package ru.practicum.compilation;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.event.EventMapper;
import ru.practicum.event.model.Event;


@Mapper(componentModel = "spring", uses = {Event.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
 public interface CompilationMapper {

    static Compilation toCompilation(CompilationCreateDto newCompilationDto) {
        return null;
    }

/*
@UtilityClass
public class CompilationMapper {
*/
    default Compilation toCompilation(CompilationCreateDto newCompilationDto, HashSet<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.getPinned() == null ? false : newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    default CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::toEventModelDto).collect(Collectors.toSet()))
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .build();
    }

    default Compilation toCompilationForUpdate(Compilation compilation,
                                              CompilationCreateDto updateCompilationDto, HashSet<Event> events) {
        return Compilation.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(updateCompilationDto
                        .getPinned() == null ? compilation.getPinned() : updateCompilationDto.getPinned())
                .title(updateCompilationDto
                        .getTitle() == null ? compilation.getTitle() : updateCompilationDto.getTitle())
                .build();
    }

}