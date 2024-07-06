package com.sh.cafekiosk.spring.api.controller.order.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
public class OrderCreateRequest {

    private List<String> productNumbers;

    @Builder
    public OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }
}
