package ru.practicum.compilation.service;

import java.util.List;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationCreateDto;

public interface CompilationService {

    CompilationDto addCompilation(CompilationCreateDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, CompilationCreateDto updateCompilationDto);

    List<CompilationDto> findCompilations(boolean pinned, int from, int size);

    CompilationDto findCompilation(Long compId);

}