package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.CategoryConstants;
import com.example.flowerShop.dto.category.CategoryDTO;
import com.example.flowerShop.dto.category.CategoryDetailedDTO;
import com.example.flowerShop.mapper.CategoryMapper;
import com.example.flowerShop.entity.Category;
import com.example.flowerShop.repository.CategoryRepository;
import com.example.flowerShop.service.CategoryService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.category.CategoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryUtils categoryUtils;

    private final CategoryMapper categoryMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    /**
     * Dependency Injection in constructor with help of @Autowired
     *
     * @param categoryRepository
     * @param categoryUtils
     * @param categoryMapper
     */
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryUtils categoryUtils,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryUtils = categoryUtils;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Retrieves list of all categories
     *
     * @return ResponseEntity<List < CategoryDTO>>
     */
    @Override
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {

        LOGGER.info("Fetching category list...");
        try {
            List<Category> categories = categoryRepository.findAll();
            LOGGER.info("Fetching completed, list of categories retrieved");
            return new ResponseEntity<>(categoryMapper.convertListToDTO(categories), HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Error while performing the fetching of the list with categories", exception);
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gets category from db by id, if it does not exist returns null
     *
     * @param id
     * @return ResponseEntity<CategoryDTO>
     */
    @Override
    public ResponseEntity<CategoryDTO> getCategoryById(UUID id) {

        LOGGER.info("Fetching category with id = " + id);
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if (categoryOptional.isPresent()) {
                Category categoryExisting = categoryOptional.get();
                LOGGER.info("Fetching completed, category retrieved");
                return new ResponseEntity<>(categoryMapper.convertToDTO(categoryExisting), HttpStatus.OK);
            } else {
                LOGGER.error("Category with id = {} not found in the db", id);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("Error while retrieving the category by id");
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a new entry in the category table
     *
     * @param categoryDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> addCategory(CategoryDetailedDTO categoryDetailedDTO) {

        LOGGER.info("Creating a new category...");
        try {
            if (this.categoryUtils.validateCategoryMap(categoryDetailedDTO)) {
                Optional<Category> categoryOptional = categoryRepository.findByName(categoryDetailedDTO.getName());
                if (categoryOptional.isEmpty()) {
                    LOGGER.info("Category created");
                    categoryRepository.save(categoryMapper.convertToEntity(categoryDetailedDTO));
                    return Utils.getResponseEntity(CategoryConstants.CATEGORY_CREATED, HttpStatus.CREATED);
                } else {
                    LOGGER.error("Category with this name already exists");
                    return Utils.getResponseEntity(CategoryConstants.CATEGORY_EXISTS, HttpStatus.BAD_REQUEST);
                }
            } else {
                LOGGER.error("Invalid data was sent for creating the category");
                return Utils.getResponseEntity(CategoryConstants.INVALID_DATA_AT_CREATING_CATEGORY, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at creating the category");
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(CategoryConstants.SOMETHING_WENT_WRONG_AT_CREATING_CATEGORY, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Updates an existing entry from the db by given id
     *
     * @param id
     * @param categoryDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> updateCategoryById(UUID id, CategoryDetailedDTO categoryDetailedDTO) {

        LOGGER.info("Updating the data for a category with id {}...", id);
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if (categoryOptional.isPresent()) {
                Category categoryExisting = categoryOptional.get();
                if (Objects.nonNull(categoryDetailedDTO.getName())) {
                    LOGGER.error("You cannot update the name of category!");
                }
                CategoryUtils.updateCategoryValues(categoryExisting, categoryDetailedDTO);
                LOGGER.info("Completed category update");
                categoryRepository.save(categoryExisting);
                return Utils.getResponseEntity(CategoryConstants.DATA_MODIFIED, HttpStatus.OK);
            } else {
                LOGGER.error("Product with id = {} not found in the db", id);
                return Utils.getResponseEntity(CategoryConstants.INVALID_CATEGORY, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at updating the product");
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(CategoryConstants.SOMETHING_WENT_WRONG_AT_UPDATING_CATEGORY, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Deletes an entry by id if found
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> deleteCategoryById(UUID id) {

        LOGGER.info("Deleting the category with id {}...", id);
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if (categoryOptional.isPresent()) {
                categoryRepository.deleteById(id);
                LOGGER.info("Category deleted successfully");
                return Utils.getResponseEntity(CategoryConstants.CATEGORY_DELETED, HttpStatus.OK);
            } else {
                LOGGER.error("Category with id = {} not found in the db", id);
                return Utils.getResponseEntity(CategoryConstants.INVALID_CATEGORY, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error("Something went wrong at deleting the category");
            e.printStackTrace();
        }
        return Utils.getResponseEntity(CategoryConstants.SOMETHING_WENT_WRONG_AT_DELETING_CATEGORY, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
