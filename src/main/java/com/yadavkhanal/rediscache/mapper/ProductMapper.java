package com.yadavkhanal.rediscache.mapper;

import com.yadavkhanal.rediscache.dto.ProductDto;
import com.yadavkhanal.rediscache.entity.Product;

public class ProductMapper {
    public static ProductDto mapToProductDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        return productDto;
    }

    public static Product mapToProduct(ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        return product;
    }

}
