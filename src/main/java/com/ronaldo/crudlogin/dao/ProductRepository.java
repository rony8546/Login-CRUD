package com.ronaldo.crudlogin.dao;

import com.ronaldo.crudlogin.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

    List<Products> findByPriceGreaterThanEqual(float price);
}
