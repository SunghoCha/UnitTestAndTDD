package com.sh.cafekiosk.spring.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
