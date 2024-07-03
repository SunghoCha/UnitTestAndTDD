package com.sh.cafekiosk.spring.api.service.order;

import com.sh.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
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

import static com.sh.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.sh.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderService orderService;

    @Test
    @DisplayName("상품번호(productNumber) 리스트를 받아 주문(OrderResponse)을 생성한다.")
    void createOrder() {
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "카페라떼", 5000);
        Product product3 = createProduct("003", HANDMADE, SELLING, "팥빙수", 10000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest orderRequest = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        //when
        OrderCreateResponse orderResponse = orderService.createOrder(orderRequest, LocalDateTime.now());

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting(OrderCreateResponse::getRegisteredDateTime, OrderCreateResponse::getTotalPrice)
                .containsExactlyInAnyOrder(
                        tuple("001", 4001),
                        tuple("002", 5001)
                );
    }

    // 생성자 한 번으로 될 일을 빌더 패턴강제되면서 억지로 만든 테스트 메서드 느낌.. 애초에 인자로 필드값 전부 넣어준다는 점에서 빌더 패턴의 장점도 사라진듯함
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

    private static void extracted() {
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
    }
}