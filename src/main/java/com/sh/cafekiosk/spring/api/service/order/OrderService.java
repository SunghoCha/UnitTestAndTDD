package com.sh.cafekiosk.spring.api.service.order;

import com.sh.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.sh.cafekiosk.spring.domain.order.Order;
import com.sh.cafekiosk.spring.domain.order.OrderRepository;
import com.sh.cafekiosk.spring.domain.product.Product;
import com.sh.cafekiosk.spring.domain.product.ProductRepository;
import com.sh.cafekiosk.spring.domain.product.ProductType;
import com.sh.cafekiosk.spring.domain.stock.Stock;
import com.sh.cafekiosk.spring.domain.stock.StockRepository;
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
    private final StockRepository stockRepository;

    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest, LocalDateTime registeredDateTime) {
        // Post 목록 조회
        List<String> productNumbers = orderCreateRequest.getProductNumbers();
        List<Product> products = findProductBy(productNumbers);

        //= 제약조건 (재고)
        // 재고 차감 체크가 필요한 상품들 필터링
        List<String> stockProductNumbers = products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .toList();

        // 재고 엔티티 조회 (* 리스트형태로 반복문 돌면 성능 떨어져서 맵으로 하는 이유 알아놓기 map이라고 표현한게 그냥 스트림 말한걸수도)
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));

        // 상품별 counting
        Map<String, Long> productCountingMap = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(productNumber -> productNumber, Collectors.counting()));

        // 재고 차감 시도
        for (Stock stock : stocks) {
            int orderQuantity = productCountingMap.get(stock.getProductNumber()).intValue();
            if (stock.isQuantityLessThan(orderQuantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다."); // deductQuantity에서 검증하는데 여기서도 하는게 익숙하진 않음
            }
            stock.deductQuantity(orderQuantity);
        }
        /*
            예외를 핸들링하고자하는 관점이 다르다고 이해해야함
            같은 것에 대한 검증이더라도 서비스단에서의 검증 목적이 사용자에게 보여주고 싶은 별도의 예외 메시지일수도 있음
            서비스단과 도메인 내부의 예외 검증은 경우에 따라 다를 여지가 있으므로 같은 검증으로 중복된 것처럼 보이더라도 따로 할 수 있음
            *이건 상황에 따라 할수도 안할수도 있다는 뜻인건가? deductQuantity가 유효성 검증 책임을 가지는건 익숙한데 서비스에서 같은 검증하는건 상황봐서 생각해볼수도..
         */
        
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
