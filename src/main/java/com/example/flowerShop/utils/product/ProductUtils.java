package com.example.flowerShop.utils.product;

import com.example.flowerShop.dto.product.ProductDTO;
import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.entity.Product;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class ProductUtils {

    public boolean validateProductMap(ProductDetailedDTO productDetailedDTO) {

        if (productDetailedDTO.getName() == null) {
            return false;
        }

        if (productDetailedDTO.getImageUrl() == null) {
            return false;
        }

        Integer stock = productDetailedDTO.getStock();
        if (stock == null || stock <= 1) {
            return false;
        }

        Double price = productDetailedDTO.getPrice();
        if (price == null || price <= 1) {
            return false;
        }

        String description = productDetailedDTO.getDescription();
        if (description == null || description.trim().isEmpty()) {
            return false;
        }

        return productDetailedDTO.getCategory() != null;
    }

    public static void updateProductValues(Product productExisting, ProductDTO product) {
        if (Objects.nonNull(product.getName()) && !"".equalsIgnoreCase(product.getName())) {
            productExisting.setName(product.getName());
        }
        if (Objects.nonNull(product.getDescription()) && !"".equalsIgnoreCase(product.getDescription())) {
            productExisting.setDescription(product.getDescription());
        }
        if (Objects.nonNull(product.getImageUrl()) && !"".equalsIgnoreCase(product.getImageUrl())) {
            productExisting.setImageUrl(product.getImageUrl());
        }
        if (Objects.nonNull(product.getPrice()) && product.getPrice() >= 5) {
            productExisting.setPrice(product.getPrice());
        }
        if (Objects.nonNull(product.getStock()) && product.getStock() >= 1) {
            productExisting.setStock(product.getStock());
        }
        if (Objects.nonNull(product.getCategory())) {
            productExisting.setCategory(product.getCategory());
        }
    }
}
