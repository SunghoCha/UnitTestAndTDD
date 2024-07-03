package com.sh.cafekiosk.spring.api.controller.product;

import com.sh.cafekiosk.spring.api.service.product.ProductService;
import com.sh.cafekiosk.spring.api.service.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("api/v1/products/selling")
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts(); // 리스트를 반환하면 유연성 떨어지는데..ㄴ
    }
}
