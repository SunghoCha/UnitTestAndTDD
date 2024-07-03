package com.sh.cafekiosk.spring.api.controller.order;

import com.sh.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.sh.cafekiosk.spring.api.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public void createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        LocalDateTime registeredDateTime = LocalDateTime.now();
        orderService.createOrder(orderCreateRequest, registeredDateTime);
    }
}
