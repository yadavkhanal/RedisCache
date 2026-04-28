package com.yadavkhanal.rediscache.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1544509566601343541L;
    @Id
    private Long id;
    private String name;
    private BigDecimal price;

}