package com.sh.cafekiosk.spring.domain.order;

import com.sh.cafekiosk.spring.domain.product.Product;
import com.sh.cafekiosk.spring.domain.product.ProductRepository;
import com.sh.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.sh.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static com.sh.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.sh.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("주문 생성 시 상품 리스트로부터 주문의 총 금액을 계산한디.")
    void calculateTotalPrice() {
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "카페라떼", 5000);
        Product product3 = createProduct("003", HANDMADE, SELLING, "팥빙수", 10000);

        //when
        Order order = Order.create(List.of(product1, product2, product3), LocalDateTime.now());

        //then
        assertThat(order.getTotalPrice()).isEqualTo(product1.getPrice() + product2.getPrice() + product3.getPrice());
    }

    @Test
    @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
    void orderStatusIsInit() {
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "카페라떼", 5000);
        Product product3 = createProduct("003", HANDMADE, SELLING, "팥빙수", 10000);

        //when
        Order order = Order.create(List.of(product1, product2, product3), LocalDateTime.now());

        //then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT); // enum비교 메서드
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