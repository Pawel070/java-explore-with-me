package ru.practicum.category.controller;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

@RestController
@Slf4j
@RequestMapping("/admin")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/categories")
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto resulting = categoryService.addCategory(categoryDto);
        log.info("CategoryAdminController addCategory: Добавление категории с названием {}.", categoryDto.getName());
        return resulting;
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto patchCategory(@RequestBody @Valid CategoryDto categoryDto,
                                     @PathVariable Long catId) {
        CategoryDto resulting = categoryService.patchCategory(catId, categoryDto);
        log.info("CategoryAdminController patchCategory: Изменение категории УИН {} --> {}.", catId, categoryDto.getName());
        return resulting;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        log.info("CategoryAdminController deleteCategory: Удаление катежории УИН {}.", catId);
    }
}