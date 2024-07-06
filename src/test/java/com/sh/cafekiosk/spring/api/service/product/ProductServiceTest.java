package com.sh.cafekiosk.spring.api.service.product;

import com.sh.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.sh.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.sh.cafekiosk.spring.domain.product.Product;
import com.sh.cafekiosk.spring.domain.product.ProductRepository;
import com.sh.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.sh.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.sh.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.sh.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;
    
    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    void createProduct() {
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 5000);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 10000);
        productRepository.saveAll(List.of(product1, product2, product3));

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(6000)
                .build();
        //when
        ProductResponse response = productService.createProduct(request);

        //then
        assertThat(response)
                .extracting(ProductResponse::getProductNumber,
                        ProductResponse::getType,
                        ProductResponse::getSellingStatus,
                        ProductResponse::getName,
                        ProductResponse::getPrice)
                .contains("004", HANDMADE, SELLING, "카푸치노", 6000);
    }
    
    @Test
    @DisplayName("등록된 상품 종류가 하나도 없는 경우, 신규 상품을 처음으로 등록하면 상품번호는 001이다.")
    void createProductWhenProductsIsEmpty() {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(6000)
                .build();

        //when
        ProductResponse response = productService.createProduct(request);

        //then
        assertThat(response)
                .extracting(ProductResponse::getProductNumber,
                        ProductResponse::getType,
                        ProductResponse::getSellingStatus,
                        ProductResponse::getName,
                        ProductResponse::getPrice)
                .contains("001", HANDMADE, SELLING, "카푸치노", 6000);
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