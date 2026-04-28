package com.yadavkhanal.rediscache.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 2464498377839842059L;
    private Long id;
    private String name;
    private BigDecimal price;
}
