package com.example.flowerShop.mapper;

import com.example.flowerShop.dto.customProduct.CustomProductDTO;
import com.example.flowerShop.dto.customProduct.CustomProductDetailedDTO;
import com.example.flowerShop.entity.CustomProduct;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.User;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomProductMapper implements Mapper<CustomProduct, CustomProductDTO, CustomProductDetailedDTO> {

    @Override
    public CustomProductDetailedDTO convertToDTO(CustomProduct customProduct) {
        if (customProduct != null) {
            return CustomProductDetailedDTO.builder()
                    .id(customProduct.getId())
                    .name(customProduct.getName())
                    .description(customProduct.getDescription())
                    .price(customProduct.getPrice())
                    .id_user(customProduct.getUser().getId())
                    .quantities(customProduct.getQuantities())
                    .id_products(this.productsToIdProducts(customProduct.getProducts()))
                    .build();
        }
        return null;
    }

    public static List<UUID> productsToIdProducts(List<Product> products) {
        return products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }

    private double calculateTotalPrice(Collection<Integer> quantities, List<Product> products) {
        double totalPrice = 0.0;
        ArrayList<Integer> quantitiesList = new ArrayList<>(quantities);
        for (int i = 0; i < products.size(); i++) {
            int quantity = quantitiesList.get(i);
            double price = products.get(i).getPrice();
            totalPrice += quantity * price;
        }
        return totalPrice;
    }

    public List<CustomProductDTO> convertListToDtoWithObjects(List<CustomProduct> source) {
        return source.stream()
                .map(this::convertEntToDtoWithObjects)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public CustomProductDTO convertEntToDtoWithObjects(CustomProduct customProduct) {

        if (customProduct != null) {
            return CustomProductDTO.builder()
                    .id(customProduct.getId())
                    .user(customProduct.getUser())
                    .products(customProduct.getProducts())
                    .price(customProduct.getPrice())
                    .name(customProduct.getName())
                    .description(customProduct.getDescription())
                    .quantities(customProduct.getQuantities())
                    .build();
        }
        return null;
    }

    public CustomProductDTO convToDtoWithObjects(CustomProductDetailedDTO customProduct, Optional<User> user, List<Product> products) {
        if (customProduct != null) {
            return CustomProductDTO.builder()
                    .id(customProduct.getId())
                    .user(user.get())
                    .name(customProduct.getName())
                    .description(customProduct.getDescription())
                    .price(customProduct.getPrice())
                    .quantities(customProduct.getQuantities())
                    .products(products)
                    .build();
        }
        return null;
    }

    @Override
    public CustomProduct convertToEntity(CustomProductDTO customProductDTO) {
        if (customProductDTO != null) {
            return CustomProduct.builder()
                    .id(customProductDTO.getId())
                    .user(customProductDTO.getUser())
                    .name(customProductDTO.getName())
                    .description(customProductDTO.getDescription())
                    .products(customProductDTO.getProducts())
                    .quantities(customProductDTO.getQuantities())
                    .price(calculateTotalPrice(customProductDTO.getQuantities(), customProductDTO.getProducts()))
                    .build();
        }
        return null;
    }
}
