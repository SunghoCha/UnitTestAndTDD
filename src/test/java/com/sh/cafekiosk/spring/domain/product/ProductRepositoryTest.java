package com.sh.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.sh.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.sh.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품의 판매상태 리스트로 상품 리스트를 조회한다")
    void findAllBySellingStatusIn() {
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 5000);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 10000);
        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());

        //then
        assertThat(products).hasSize(2)
                .extracting(Product::getProductNumber, Product::getName, Product::getSellingStatus)
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }
    
    @Test
    @DisplayName("상품의 상품번호 리스트로 상품 리스트를 조회한다.")
    void findAllByProductNumberIn() {
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "카페라떼", 5000);
        Product product3 = createProduct("003", HANDMADE, SELLING, "팥빙수", 10000);
        productRepository.saveAll(List.of(product1, product2, product3));
        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));
        //then
        assertThat(products).hasSize(2)
                .extracting(Product::getProductNumber, Product::getName, Product::getSellingStatus)
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", SELLING)
                );
    }

    private Product createProduct(String productNumber,
                                  ProductType productType,
                                  ProductSellingStatus productSellingStatus,
                                  String name,
                                  int price) {

        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(productSellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}