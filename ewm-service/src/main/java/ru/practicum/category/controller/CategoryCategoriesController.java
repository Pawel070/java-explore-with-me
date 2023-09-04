package ru.practicum.category.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryCategoriesController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryCategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> findCategories(@RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        List<CategoryDto> resulting = categoryService.findCategories(from, size);
        log.info("CategoryCategoriesController findCategories: Cписок категорий --> {}.", resulting);
        return resulting;
    }

    @GetMapping("/{catId}")
    public CategoryDto findCategoryById(@PathVariable Long catId) {
        CategoryDto resulting = categoryService.findCategoryById(catId);
        log.info("CategoryCategoriesController findCategoryById:  Категория с УИН {} --> {} ", catId, resulting);
        return resulting;
    }

}