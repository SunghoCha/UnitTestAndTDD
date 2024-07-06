package com.sh.cafekiosk.spring.api.service.product;

import com.sh.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.sh.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.sh.cafekiosk.spring.domain.product.Product;
import com.sh.cafekiosk.spring.domain.product.ProductRepository;
import com.sh.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.sh.cafekiosk.spring.domain.product.ProductType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sh.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.sh.cafekiosk.spring.domain.product.ProductType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .toList();
    }

    public ProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = createNextProductNumber();
        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProduct();
        if (latestProductNumber == null) {
            return "001";
        }
        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;
        return  String.format("%03d", nextProductNumberInt);
    }
}
