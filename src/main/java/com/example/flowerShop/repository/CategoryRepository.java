package com.example.flowerShop.repository;

import com.example.flowerShop.entity.Category;
import com.example.flowerShop.utils.category.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findById(@Param("id") UUID id);

    Optional<Category> findByName(CategoryName name);

}
