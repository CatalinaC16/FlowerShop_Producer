package com.example.flowerShop.service;

import com.example.flowerShop.dto.customProduct.CustomProductDTO;
import com.example.flowerShop.dto.customProduct.CustomProductDetailedDTO;
import com.example.flowerShop.dto.product.ProductDetailedDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CustomProductService {

    ResponseEntity<List<CustomProductDTO>> getAllCustomProducts();

    ResponseEntity<CustomProductDTO> getCustomProductById(UUID id);

    ResponseEntity<String> addCustomProduct(CustomProductDetailedDTO productDetailedDTO);

    ResponseEntity<String> deleteCustomProductById(UUID id);

    ResponseEntity<List<ProductDetailedDTO>> getAllProducts();
}
