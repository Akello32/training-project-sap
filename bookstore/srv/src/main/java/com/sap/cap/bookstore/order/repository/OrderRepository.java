package com.sap.cap.bookstore.order.repository;

import com.sap.cds.ql.Select;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.services.persistence.PersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import cds.gen.ordersservice.OrderItems;
import cds.gen.ordersservice.OrderItems_;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final PersistenceService db;

    public List<OrderItems> findOrderItemByOrderId(String orderId) {
        CqnSelect selItems = Select.from(OrderItems_.class).where(i -> i.parent().ID().eq(orderId));

        return db.run(selItems).listOf(OrderItems.class);
    }
}
