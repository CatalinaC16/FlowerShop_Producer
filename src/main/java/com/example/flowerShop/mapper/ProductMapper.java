package com.example.flowerShop.mapper;

import com.example.flowerShop.dto.product.ProductDTO;
import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.entity.Category;
import com.example.flowerShop.entity.Product;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductMapper implements Mapper<Product, ProductDTO, ProductDetailedDTO> {

    @Override
    public ProductDetailedDTO convertToDTO(Product product) {

        if (product != null) {
            return ProductDetailedDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .imageUrl(product.getImageUrl())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .category(String.valueOf(product.getCategory().getName()))
                    .build();
        }
        return null;
    }

    @Override
    public Product convertToEntity(ProductDTO productDTO) {

        if (productDTO != null) {
            return Product.builder()
                    .id(productDTO.getId())
                    .name(productDTO.getName())
                    .description(productDTO.getDescription())
                    .imageUrl(productDTO.getImageUrl())
                    .price(productDTO.getPrice())
                    .stock(productDTO.getStock())
                    .category(productDTO.getCategory())
                    .build();
        }
        return null;
    }

    public ProductDTO convToProdWithCategory(ProductDetailedDTO productDetailedDTO, Optional<Category> category) {

        if (productDetailedDTO != null) {
            return ProductDTO.builder()
                    .id(productDetailedDTO.getId())
                    .name(productDetailedDTO.getName())
                    .description(productDetailedDTO.getDescription())
                    .imageUrl(productDetailedDTO.getImageUrl())
                    .price(productDetailedDTO.getPrice())
                    .stock(productDetailedDTO.getStock())
                    .category(category.get())
                    .build();
        }
        return null;
    }
}
