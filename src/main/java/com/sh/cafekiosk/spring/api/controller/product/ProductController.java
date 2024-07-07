package com.sh.cafekiosk.spring.api.controller.product;

import com.sh.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.sh.cafekiosk.spring.api.dto.response.ApiResponse;
import com.sh.cafekiosk.spring.api.service.product.ProductService;
import com.sh.cafekiosk.spring.api.service.product.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("api/v1/products/selling")
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts(); // 리스트를 반환하면 유연성 떨어지는데..ㄴ
    }

    @PostMapping("api/v1/products/new")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        log.info("request :{}", request);
        ProductResponse response1 = productService.createProduct(request);
        log.info("response1 : {}", response1.getPrice());
        ApiResponse<ProductResponse> apiResponse = ApiResponse.of(HttpStatus.OK, response1);
        log.info("ApiResponse : {}", apiResponse.getData());
        return apiResponse;
    }
}
