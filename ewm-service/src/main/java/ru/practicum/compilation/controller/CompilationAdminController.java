package ru.practicum.compilation.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.service.ValidationInterface;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @Validated({ValidationInterface.AddCompilation.class})
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid CompilationCreateDto newCompilationDto) {
        CompilationDto resulting = compilationService.addCompilation(newCompilationDto);
        log.info("CompilationAdminController addCompilation: Добавлена новая подборка событий --> {} ", resulting);
        return resulting;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        log.info("CompilationAdminController deleteCompilation: Удаляется подборка событиий УИН {}", compId);
    }

    @PatchMapping("/{compId}")
    @Validated({ValidationInterface.UpCompilation.class})
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody @Valid CompilationCreateDto updateCompilationDto) {
        CompilationDto resulting = compilationService.updateCompilation(compId, updateCompilationDto);
        log.info("CompilationAdminController updateCompilation: Изменена подборка событий --> {}", resulting);
        return resulting;
    }

}