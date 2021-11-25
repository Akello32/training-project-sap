package com.sap.cap.bookstore.order.handler;

import cds.gen.ordersservice.OrderItems;
import cds.gen.ordersservice.OrderItems_;
import cds.gen.ordersservice.Orders;
import cds.gen.ordersservice.OrdersService_;
import cds.gen.ordersservice.Orders_;
import cds.gen.sap.capire.bookstore.Books;
import com.sap.cap.bookstore.book.service.BookService;
import com.sap.cap.bookstore.order.service.OrderService;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@ServiceName(OrdersService_.CDS_NAME)
@RequiredArgsConstructor
public class OrdersHandler implements EventHandler {
    private final BookService bookService;
    private final OrderService orderService;

    @Before(event = CdsService.EVENT_CREATE, entity = OrderItems_.CDS_NAME)
    public void validateBookAndDecreaseStock(List<OrderItems> items) {
        for (OrderItems item : items) {
            String bookId = item.getBookId();
            Integer amount = item.getAmount();

            Books book = bookService.getBookWithStock(bookId);

            int stock = Optional.of(book.getStock())
                    .orElseThrow(() -> new ServiceException(ErrorStatuses.BAD_REQUEST, "Not enough books on stock"));

            book.setStock(stock - amount);
            bookService.updateBook(book);
        }
    }

    @Before(event = CdsService.EVENT_CREATE, entity = Orders_.CDS_NAME)
    public void validateBookAndDecreaseStockViaOrders(List<Orders> orders) {
        for (Orders order : orders) {
            if (order.getItems() != null) {
                validateBookAndDecreaseStock(order.getItems());
            }
        }
    }

    @After(event = {CdsService.EVENT_READ, CdsService.EVENT_CREATE}, entity = OrderItems_.CDS_NAME)
    public void calculateNetAmount(List<OrderItems> items) {
        for (OrderItems item : items) {
            String bookId = item.getBookId();

            Books book = bookService.getBook(bookId);

            item.setNetAmount(book.getPrice().multiply(new BigDecimal(item.getAmount())));
        }
    }

    @After(event = {CdsService.EVENT_READ, CdsService.EVENT_CREATE}, entity = Orders_.CDS_NAME)
    public void calculateTotal(List<Orders> orders) {
        for (Orders order : orders) {
            if (order.getItems() != null) {
                calculateNetAmount(order.getItems());
            }

            List<OrderItems> allItems = orderService.getOrderItemByOrderId(order.getId());

            calculateNetAmount(allItems);

            BigDecimal total = new BigDecimal(0);
            for (OrderItems item : allItems) {
                total = total.add(item.getNetAmount());
            }
            order.setTotal(total);
        }
    }
}