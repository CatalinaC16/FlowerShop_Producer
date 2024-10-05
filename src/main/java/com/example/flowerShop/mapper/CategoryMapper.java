package com.example.flowerShop.mapper;

import com.example.flowerShop.dto.category.CategoryDTO;
import com.example.flowerShop.dto.category.CategoryDetailedDTO;
import com.example.flowerShop.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements Mapper<Category, CategoryDetailedDTO, CategoryDTO> {

    @Override
    public CategoryDTO convertToDTO(Category category) {

        if (category != null) {
            return CategoryDTO.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .products(category.getProducts())
                    .description(category.getDescription())
                    .build();
        }
        return null;
    }

    @Override
    public Category convertToEntity(CategoryDetailedDTO categoryDetailedDTO) {

        if (categoryDetailedDTO != null) {
            return Category.builder()
                    .id(categoryDetailedDTO.getId())
                    .name(categoryDetailedDTO.getName())
                    .description(categoryDetailedDTO.getDescription())
                    .products(categoryDetailedDTO.getProducts())
                    .build();
        }
        return null;
    }
}