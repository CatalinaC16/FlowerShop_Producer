package com.example.flowerShop.controller;

import com.example.flowerShop.dto.category.CategoryDTO;
import com.example.flowerShop.dto.category.CategoryDetailedDTO;
import com.example.flowerShop.service.impl.CategoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {

    private final CategoryServiceImpl categoryServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    /**
     * Dep Injection in constructor with the help of Spring annotation
     *
     * @param categoryServiceImpl
     */
    @Autowired
    public CategoryController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }

    /**
     * Retrieves list of categories
     *
     * @return ResponseEntity<List < CategoryDTO>>
     */
    @GetMapping("/get/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        LOGGER.info("Request for list of categories");
        return this.categoryServiceImpl.getAllCategories();
    }

    /**
     * Gets category by id
     *
     * @param id
     * @return ResponseEntity<CategoryDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable UUID id) {
        LOGGER.info("Request for category by id");
        return this.categoryServiceImpl.getCategoryById(id);
    }

    /**
     * Creates a new category
     *
     * @param categoryDetailedDTO
     * @return ResponseEntity<String>
     */
    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody CategoryDetailedDTO categoryDetailedDTO) {
        LOGGER.info("Request for creating a new category");
        return this.categoryServiceImpl.addCategory(categoryDetailedDTO);
    }

    /**
     * Updates category by id
     *
     * @param id
     * @param categoryDetailedDTO
     * @return ResponseEntity<String>
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategoryById(@PathVariable UUID id, @RequestBody CategoryDetailedDTO categoryDetailedDTO) {
        LOGGER.info("Request for updating a category by id");
        return this.categoryServiceImpl.updateCategoryById(id, categoryDetailedDTO);
    }

    /**
     * Deletes category
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable UUID id) {
        LOGGER.info("Request for deleting a category by id");
        return this.categoryServiceImpl.deleteCategoryById(id);
    }
}
