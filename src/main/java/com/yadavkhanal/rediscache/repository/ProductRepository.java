package com.yadavkhanal.rediscache.repository;

import com.yadavkhanal.rediscache.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}