package com.yadavkhanal.rediscache.controller;

import com.yadavkhanal.rediscache.dto.ProductDto;
import com.yadavkhanal.rediscache.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService service;

  @GetMapping("/{id}")
  public ProductDto get(@PathVariable Long id){
      return service.getProduct(id);
  }
}