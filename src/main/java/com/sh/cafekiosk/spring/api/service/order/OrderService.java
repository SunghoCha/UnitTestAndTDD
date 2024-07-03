package com.sh.cafekiosk.spring.api.service.order;

import com.sh.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.sh.cafekiosk.spring.domain.order.Order;
import com.sh.cafekiosk.spring.domain.order.OrderRepository;
import com.sh.cafekiosk.spring.domain.product.Product;
import com.sh.cafekiosk.spring.domain.product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest, LocalDateTime registeredDateTime) {
        // Post 목록 조회
        List<String> productNumbers = orderCreateRequest.getProductNumbers();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        Order.create(products, registeredDateTime);
        return null;
    }
}
