package com.sh.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {

    @Test
    @DisplayName("재고 관련없는 상품 타입으로 재고 관련 타입 체크 후 false를 반환한다.")
    void notContainsStockType() {
        //given
        ProductType givenType = ProductType.HANDMADE;

        //when
        boolean isContained = ProductType.containsStockType(givenType);

        //then
        assertThat(isContained).isFalse();
    }

    @Test
    @DisplayName("재고 관련있는 상품 타입으로 재고 관련 타입 체크 후 true를 반환한다.")
    void containsStockType() {
        //given
        ProductType givenType = ProductType.BAKERY;

        //when
        boolean isContained = ProductType.containsStockType(givenType);

        //then
        assertThat(isContained).isTrue();
    }
}