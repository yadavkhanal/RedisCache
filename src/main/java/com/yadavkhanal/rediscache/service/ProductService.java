package com.yadavkhanal.rediscache.service;

import com.yadavkhanal.rediscache.dto.ProductDto;
import com.yadavkhanal.rediscache.entity.Product;
import com.yadavkhanal.rediscache.mapper.ProductMapper;
import com.yadavkhanal.rediscache.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository repository;

    @Cacheable(value = "products", key = "#id", sync = true)
    public ProductDto getProduct(Long id) {
        log.info("going to db for product..");
        Product product = repository.findById(id).orElse(null);
        if (product != null) {
            return ProductMapper.mapToProductDto(product);
        }
        return null;
    }

    @CachePut(value = "products", key = "#product.id")
    public Product update(Product product) {
        return repository.save(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }

}