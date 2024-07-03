package com.sh.cafekiosk.spring.domain.product;

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
    @DisplayName("원하는 판매상태를 리스트로 전달하면 해당 상품 리스트를 반환한다")
    void findAllBySellingStatusIn() {
        //given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(5000)
                .build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(10000)
                .build();
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
}