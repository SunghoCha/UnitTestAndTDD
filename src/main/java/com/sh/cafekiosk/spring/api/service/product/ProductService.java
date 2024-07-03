package com.sh.cafekiosk.spring.api.service.product;

import com.sh.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.sh.cafekiosk.spring.domain.product.Product;
import com.sh.cafekiosk.spring.domain.product.ProductRepository;
import com.sh.cafekiosk.spring.domain.product.ProductSellingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(product -> ProductResponse.of(product))
                .toList();
    }
}
