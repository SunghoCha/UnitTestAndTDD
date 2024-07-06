package com.sh.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    @DisplayName("재고가 주문수량보다 적으면 true를 반환한다")
    void isQuantityLessThan() {
        //given
        Stock stock = Stock.create("001", 1);
        int orderQuantity = 2;

        //when
        boolean isQuantityLessThan = stock.isQuantityLessThan(orderQuantity);

        //then
        assertThat(isQuantityLessThan).isTrue();
    }

    @Test
    @DisplayName("재고가 주문수량 이상이면 false를 반환한다")
    void isNotQuantityLessThan() {
        //given
        Stock stock = Stock.create("001", 10);
        int lessOrderQuantity = 2;
        int sameOrderQuantity = 10;

        //when
        boolean isQuantityLessThan = stock.isQuantityLessThan(lessOrderQuantity);
        boolean isQuantityLessThan2 = stock.isQuantityLessThan(sameOrderQuantity);

        //then
        assertThat(isQuantityLessThan).isFalse();
        assertThat(isQuantityLessThan2).isFalse();
    }

    @Test
    @DisplayName("재고를 주어진 개수만큼 차감한다.")
    void deductQuantity() {
        //given
        Stock stock = Stock.create("001", 10);
        int orderQuantity = 10;
        //when
        stock.deductQuantity(orderQuantity);
        //then
        assertThat(stock.getQuantity()).isZero();
    }

    @Test
    @DisplayName("재고를 초과하는 주문수량으로 차감 시도하면 예외가 발생한다.")
    void deductQuantityWithException() {
        //given
        Stock stock = Stock.create("001", 10);
        int orderQuantity = 100;
        
        //expected
        assertThatThrownBy(() -> stock.deductQuantity(orderQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("차감할 재고 수량이 없습니다.");

    }
}