package ru.practicum.category.service;

import java.util.List;

import ru.practicum.category.dto.CategoryDto;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto patchCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);

    List<CategoryDto> findCategories(int from, int size);

    CategoryDto findCategoryById(Long id);

}