package com.sap.cap.bookstore.order.service;

import com.sap.cap.bookstore.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import cds.gen.ordersservice.OrderItems;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;

    @Transactional
    public List<OrderItems> getOrderItemByOrderId(String orderId) {
        return repository.findOrderItemByOrderId(orderId);
    }
}
