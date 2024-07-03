package com.sh.cafekiosk.spring.domain.product;

import com.sh.cafekiosk.spring.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 프록시한테는 접근권한 줘야함
@Entity
public class Product extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String productNumber;

    @Enumerated(EnumType.STRING) // 인덱스로 하면 Enum value 변경 시에 문제될 수 있음
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;

    @Builder
    public Product(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }
}
