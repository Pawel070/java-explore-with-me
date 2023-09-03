package ru.practicum.compilation.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

import ru.practicum.event.service.ValidationInterface;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationCreateDto {

    private List<Long> events;

    private Boolean pinned;

    @NotBlank(groups = ValidationInterface.AddCompilation.class)
    @Size(min = 1, max = 50)
    private String title;

}