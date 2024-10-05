package com.example.flowerShop.service;

import com.example.flowerShop.dto.category.CategoryDTO;
import com.example.flowerShop.dto.category.CategoryDetailedDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    ResponseEntity<List<CategoryDTO>> getAllCategories();

    ResponseEntity<CategoryDTO> getCategoryById(UUID id);

    ResponseEntity<String> addCategory(CategoryDetailedDTO categoryDetailedDTO);

    ResponseEntity<String> updateCategoryById(UUID id, CategoryDetailedDTO categoryDetailedDTO);

    ResponseEntity<String> deleteCategoryById(UUID id);
}
