package ru.practicum.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.category.CategoryMapper;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.EventRepository;
import ru.practicum.exceptions.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        log.info("CategoryServiceImpl addCategory: Добавление категории {} ", category);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDto patchCategory(Long id, CategoryDto categoryDto) {
        categoryDto.setId(id);
        Category category = CategoryMapper.toCategoryForPatch(categoryDto);
        log.info("CategoryServiceImpl addCategory: Изменение категории {} ", category);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        if (eventRepository.existsByCategoryId(id)) {
            throw new DataIntegrityViolationException("Отказ. Категория уже связана с событием.");
        }
        log.info("CategoryServiceImpl deleteCategory: Удаление категории УИН {}.", id);
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> findCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        List<CategoryDto> resulting = new ArrayList<>();
        if (!categories.isEmpty()) {
            resulting.addAll(categories.stream().map(category -> CategoryMapper.toCategoryDto(category)).collect(Collectors.toList()));
        }
        log.info("CategoryServiceImpl findCategories: Найдены категории {} ", resulting);
        return resulting;
    }

    @Override
    public CategoryDto findCategoryById(Long id) {
        log.info("CategoryServiceImpl findCategoryById: Поиск категории по УИН {} ", id);
        return CategoryMapper.toCategoryDto(categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("")));
    }

}