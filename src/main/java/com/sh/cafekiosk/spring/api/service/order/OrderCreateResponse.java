package com.sh.cafekiosk.spring.api.service.order;

import com.sh.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.sh.cafekiosk.spring.domain.order.Order;
import com.sh.cafekiosk.spring.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderCreateResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    private List<ProductResponse> productResponses;

    @Builder
    private OrderCreateResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> productResponses) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.productResponses = productResponses;
    }

    public static OrderCreateResponse of(Order order) {
        return OrderCreateResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .productResponses(order.getOrderProducts().stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .collect(Collectors.toList()))
                .build();
    }
}
