package com.example.flowerShop.utils.category;

import com.example.flowerShop.dto.category.CategoryDetailedDTO;
import com.example.flowerShop.entity.Category;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class CategoryUtils {

    public boolean validateCategoryMap(CategoryDetailedDTO categoryDetailedDTO) {
        return !Objects.equals(categoryDetailedDTO.getName(), "")
                && !Objects.equals(categoryDetailedDTO.getDescription(), "");
    }

    public static void updateCategoryValues(Category categoryExisting, CategoryDetailedDTO category) {
        if (Objects.nonNull(category.getDescription()) && !"".equalsIgnoreCase(category.getDescription())) {
            categoryExisting.setDescription(category.getDescription());
        }
    }
}