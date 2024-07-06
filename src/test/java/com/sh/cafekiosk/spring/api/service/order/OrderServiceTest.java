package com.sh.cafekiosk.spring.api.service.order;

import com.sh.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.sh.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.sh.cafekiosk.spring.domain.order.OrderProductRepository;
import com.sh.cafekiosk.spring.domain.order.OrderRepository;
import com.sh.cafekiosk.spring.domain.product.Product;
import com.sh.cafekiosk.spring.domain.product.ProductRepository;
import com.sh.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.sh.cafekiosk.spring.domain.product.ProductType;
import com.sh.cafekiosk.spring.domain.stock.Stock;
import com.sh.cafekiosk.spring.domain.stock.StockRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.sh.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.sh.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
//@Transactional
class OrderServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    OrderService orderService;

    @AfterEach
    void tearDown() {
        // deleteAll() 과의 차이점은? // 외래키조건 때문에 삭제 순서도 중요한듯...
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("상품번호(productNumber) 리스트로 주문(OrderResponse)을 생성한다.")
    void createOrder() {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "카페라떼", 5000);
        Product product3 = createProduct("003", HANDMADE, SELLING, "팥빙수", 10000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest orderRequest = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        //when
        OrderCreateResponse orderResponse = orderService.createOrder(orderRequest, registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting(OrderCreateResponse::getRegisteredDateTime, OrderCreateResponse::getTotalPrice)
                .contains(registeredDateTime, 9000);
        assertThat(orderResponse.getProductResponses()).hasSize(2)
                .extracting(ProductResponse::getProductNumber, ProductResponse::getPrice)
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("002", 5000)
                );
    }

    @Test
    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    void createOrderWithDuplicateProductNumbers() {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "카페라떼", 5000);
        Product product3 = createProduct("003", HANDMADE, SELLING, "팥빙수", 10000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest orderRequest = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        //when
        OrderCreateResponse orderResponse = orderService.createOrder(orderRequest, registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting(OrderCreateResponse::getRegisteredDateTime, OrderCreateResponse::getTotalPrice)
                .contains(registeredDateTime, 8000);
        assertThat(orderResponse.getProductResponses()).hasSize(2)
                .extracting(ProductResponse::getProductNumber, ProductResponse::getPrice)
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000)
                );
    }

    @Test
    @DisplayName("재고 개념이 있는 상품이 포함된 상품번호 리스트로 주문(OrderResponse)을 생성한다.")
    void createOrderWithStock() {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct("001", BOTTLE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", BAKERY, SELLING, "카페라떼", 5000);
        Product product3 = createProduct("003", HANDMADE, SELLING, "팥빙수", 10000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest orderRequest = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();
        //when
        OrderCreateResponse orderResponse = orderService.createOrder(orderRequest, registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting(OrderCreateResponse::getRegisteredDateTime, OrderCreateResponse::getTotalPrice)
                .contains(registeredDateTime, 23000);
        assertThat(orderResponse.getProductResponses()).hasSize(4)
                .extracting(ProductResponse::getProductNumber, ProductResponse::getPrice)
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000),
                        tuple("002", 5000),
                        tuple("003", 10000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );
    }

    @Test
    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    void createOrderWithNoStock() { // WithoutStock? WithNoStock?
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct("001", BOTTLE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", BAKERY, SELLING, "카페라떼", 5000);
        Product product3 = createProduct("003", HANDMADE, SELLING, "팥빙수", 10000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 0);
        Stock stock2 = Stock.create("002", 0);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest orderRequest = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();
        //expected
        assertThatThrownBy(() -> orderService.createOrder(orderRequest, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");


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
}