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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest, LocalDateTime registeredDateTime) {
        // Post 목록 조회
        List<String> productNumbers = orderCreateRequest.getProductNumbers();

        List<Product> products = findProductBy(productNumbers);

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderCreateResponse.of(savedOrder);
    }

    private List<Product> findProductBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, product -> product));
        return productNumbers.stream() // 이렇게 코딩하면 개수가 엄청나게 많은 경우는 어떻게 되는거지
                .map(productMap::get)
                .toList()
                ;
    }
}
