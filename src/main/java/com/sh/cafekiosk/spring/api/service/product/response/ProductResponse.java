package com.sh.cafekiosk.spring.api.service.product.response;

import com.sh.cafekiosk.spring.domain.product.Product;
import com.sh.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.sh.cafekiosk.spring.domain.product.ProductType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String productNumber;
    private ProductType type;
    private ProductSellingStatus SellingStatus;
    private String name;
    private int price;

    @Builder
    public ProductResponse(Long id, String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.type = type;
        this.SellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .type(product.getType())
                .sellingStatus(product.getSellingStatus())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
