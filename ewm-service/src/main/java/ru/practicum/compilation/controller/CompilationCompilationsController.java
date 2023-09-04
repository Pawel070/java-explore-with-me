package ru.practicum.compilation.controller;

import java.util.List;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationCompilationsController {
    private final CompilationService compilationService;

    @GetMapping
    List<CompilationDto> findCompilations(@RequestParam(required = false) boolean pinned,
                                          @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        List<CompilationDto> resulting = compilationService.findCompilations(pinned, from, size);
        log.info("CompilationCompilationsController findCompilations: Получение списка подборок событий --> {} ", resulting);
        return resulting;
    }

    @GetMapping("/{compId}")
    CompilationDto findCompilation(@PathVariable Long compId) {
        CompilationDto resulting = compilationService.findCompilation(compId);
        log.info("CompilationCompilationsController findCompilation: Получение подборки событий --> {} ", resulting);
        return resulting;
    }

}