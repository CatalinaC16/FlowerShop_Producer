package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.CustomProductConstants;
import com.example.flowerShop.constants.ShoppingCartConstants;
import com.example.flowerShop.dto.customProduct.CustomProductDTO;
import com.example.flowerShop.dto.customProduct.CustomProductDetailedDTO;;
import com.example.flowerShop.dto.orderItem.OrderItemDTO;
import com.example.flowerShop.dto.orderItem.OrderItemDetailedDTO;
import com.example.flowerShop.dto.product.ProductDTO;
import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.entity.*;
import com.example.flowerShop.mapper.CustomProductMapper;
import com.example.flowerShop.mapper.OrderItemMapper;
import com.example.flowerShop.mapper.ProductMapper;
import com.example.flowerShop.repository.*;
import com.example.flowerShop.service.CustomProductService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.category.CategoryName;
import com.example.flowerShop.utils.customProduct.CustomProductUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomProductServiceImpl implements CustomProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CustomProductMapper customProductMapper;
    private final CategoryRepository categoryRepository;
    private final CustomProductRepository customProductRepository;
    private final CustomProductUtils customProductUtils;
    private final ProductMapper productMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    public CustomProductServiceImpl(ProductRepository productRepository, CustomProductMapper customProductMapper, CustomProductRepository customProductRepository, CustomProductUtils customProductUtils, UserRepository userRepository, ProductMapper productMapper, CategoryRepository categoryRepository, OrderItemMapper orderItemMapper, OrderItemRepository orderItemRepository, ShoppingCartRepository shoppingCartRepository) {
        this.customProductMapper = customProductMapper;
        this.customProductRepository = customProductRepository;
        this.productRepository = productRepository;
        this.customProductUtils = customProductUtils;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.orderItemMapper = orderItemMapper;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public ResponseEntity<List<CustomProductDTO>> getAllCustomProducts() {
        try {
            List<CustomProduct> customProducts = customProductRepository.findAll();
            return new ResponseEntity<>(customProductMapper.convertListToDtoWithObjects(customProducts), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<CustomProductDTO> getCustomProductById(UUID id) {
        try {
            Optional<CustomProduct> customProduct = customProductRepository.findById(id);
            if (customProduct.isPresent()) {
                CustomProduct customProductEx = customProduct.get();
                return new ResponseEntity<>(customProductMapper.convertEntToDtoWithObjects(customProductEx), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> addCustomProduct(CustomProductDetailedDTO customProductDetailedDTO) {
        try {
            if (!this.customProductUtils.validateCustomProductMap(customProductDetailedDTO)) {
                return Utils.getResponseEntity(CustomProductConstants.INVALID_DATA_AT_CREATING_CUSTOM_PRODUCT, HttpStatus.BAD_REQUEST);
            }
            Optional<User> user = userRepository.findById(customProductDetailedDTO.getId_user());
            List<Product> products = productRepository.findProjectedByIdIn(customProductDetailedDTO.getId_products());
            this.decreaseQuantitiesOfProducts(products, (List<Integer>) customProductDetailedDTO.getQuantities());
            if (!areProductsValid(products)) {
                return Utils.getResponseEntity(CustomProductConstants.SOMETHING_WENT_WRONG_AT_CREATING_CUSTOM_PRODUCT, HttpStatus.BAD_REQUEST);
            }
            CustomProductDTO customProductDTO = customProductMapper.convToDtoWithObjects(customProductDetailedDTO, user, products);
            CustomProduct customProduct = saveCustomProduct(customProductDTO);
            Product product = createProduct(customProductDTO, customProduct);
            session.setAttribute("quantity_" + product.getId(), customProductDetailedDTO.getQuantities());
            session.setAttribute("products_" + product.getId(), products);
            OrderItem orderItem = createOrderItem(product);
            updateShoppingCart(user, orderItem, product);
            cleanUpCustomProduct(customProduct, product);
            return Utils.getResponseEntity(CustomProductConstants.CUSTOM_PRODUCT_CREATED, HttpStatus.CREATED);
        } catch (Exception exception) {
            exception.printStackTrace();
            return Utils.getResponseEntity(CustomProductConstants.SOMETHING_WENT_WRONG_AT_CREATING_CUSTOM_PRODUCT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void decreaseQuantitiesOfProducts(List<Product> products, List<Integer> quantities) {
        if (products.size() != quantities.size()) {
            throw new IllegalArgumentException("The size of products and quantities must be the same.");
        }
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int newQuantity = product.getStock() - quantities.get(i);
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative.");
            }
            product.setStock(newQuantity);
            productRepository.save(product);
        }
    }

    private boolean areProductsValid(List<Product> products) {
        return products.stream().allMatch(Objects::nonNull) && !products.isEmpty();
    }

    private CustomProduct saveCustomProduct(CustomProductDTO customProductDTO) {
        return customProductRepository.save(customProductMapper.convertToEntity(customProductDTO));
    }

    private Product createProduct(CustomProductDTO customProductDTO, CustomProduct customProduct) {
        Optional<Category> category = categoryRepository.findByName(CategoryName.valueOf(String.valueOf(CategoryName.CUSTOM_BOUQUETS)));
        ProductDetailedDTO productDetailedDTO = new ProductDetailedDTO(null, customProductDTO.getName(), customProductDTO.getDescription(), "/images/custom.jpg", customProduct.getPrice(), 1, String.valueOf(CategoryName.CUSTOM_BOUQUETS));
        ProductDTO productDTO = productMapper.convToProdWithCategory(productDetailedDTO, category);
        return productRepository.save(productMapper.convertToEntity(productDTO));
    }

    private OrderItem createOrderItem(Product product) {
        OrderItemDTO orderItemDTO = new OrderItemDTO(null, 1, product);
        return orderItemRepository.save(orderItemMapper.convertToEntity(orderItemDTO));
    }

    private void updateShoppingCart(Optional<User> user, OrderItem orderItem, Product product) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUser(user.get());
        if (shoppingCart.isPresent()) {
            ShoppingCart existingCart = shoppingCart.get();
            List<OrderItem> items = existingCart.getOrderItems();
            items.add(orderItem);
            existingCart.setTotalPrice((long) (existingCart.getTotalPrice() + product.getPrice()));
            shoppingCartRepository.save(existingCart);
        }
    }

    private void cleanUpCustomProduct(CustomProduct customProduct, Product product) {
        customProductRepository.delete(customProduct);
        product.setStock(0);
        productRepository.save(product);
    }


    @Override
    public ResponseEntity<String> deleteCustomProductById(UUID id) {
        try {
            Optional<CustomProduct> customProduct = customProductRepository.findById(id);
            if (customProduct.isPresent()) {
                customProductRepository.deleteById(id);
                return Utils.getResponseEntity(CustomProductConstants.CUSTOM_PRODUCT_DELETED, HttpStatus.OK);
            } else {
                return Utils.getResponseEntity(CustomProductConstants.INVALID_CUSTOM_PRODUCT, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(CustomProductConstants.SOMETHING_WENT_WRONG_AT_DELETING_CUSTOM_PRODUCT, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<ProductDetailedDTO>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return new ResponseEntity<>(productMapper.convertListToDTO(products), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
