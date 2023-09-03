package ru.practicum.category;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;


@Mapper(componentModel = "spring", uses = {Event.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
 public interface CategoryMapper {

    static Category toCategory(CategoryDto categoryDto) {
        return null;
    }

    static CategoryDto toCategoryDto(Category category) {
        return null;
    }

    static Category toCategoryForPatch(CategoryDto categoryDto) {
        return null;
    }

}
/*
@Service
public class CategoryMapper {

    public Category toCategory(CategoryDto categoryDto) {
        return Category.builder().name(categoryDto.getName()).build();
    }

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder().id(category.getId()).name(category.getName()).build();
    }

    public Category toCategoryForPatch(CategoryDto categoryDto) {
        return Category.builder().id(categoryDto.getId()).name(categoryDto.getName()).build();
    }

}

 */