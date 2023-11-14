package cz.muni.fi.orderService.sampledata;

import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.entity.OrderItem;
import cz.muni.fi.orderService.enums.OrderState;
import cz.muni.fi.orderService.service.OrderService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class SampleDataLoading {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoading.class);

    @Autowired
    private OrderService orderService;

    private static Date daysBeforeNow(int days) {
        return Date.from(ZonedDateTime.now().minusDays(days).toInstant());
    }

    private void createOrder(Long userId, Date created, OrderState state, OrderItem... items) {
        Order order = new Order();
        order.setUserId(userId);
        order.setCreated(created);
        order.setState(state);
        for (OrderItem item : items) {
            order.addOrderItem(item);
        }
        orderService.createOrder(order);
    }

    private OrderItem createOrderItem(Long productId, int amount) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(productId);
        orderItem.setAmount(amount);
        return orderItem;
    }
    @PostConstruct
    public void loadProductSampleData() {
        //TODO HTTP calls to get IDs

        createOrder(1L, daysBeforeNow(10), OrderState.DONE, createOrderItem(24L, 5), createOrderItem(10L, 1));
        createOrder(1L, daysBeforeNow(6), OrderState.SHIPPED, createOrderItem(13L, 3), createOrderItem(8L, 3));
        createOrder(1L, daysBeforeNow(3), OrderState.CANCELED, createOrderItem(24L, 10), createOrderItem(13L, 1));
        createOrder(1L, daysBeforeNow(2), OrderState.RECEIVED, createOrderItem(24L, 10), createOrderItem(13L, 1));
        createOrder(2L, daysBeforeNow(1), OrderState.RECEIVED, createOrderItem(24L, 1), createOrderItem(13L, 1), createOrderItem(8L, 1));
        createOrder(3L, daysBeforeNow(1), OrderState.RECEIVED, createOrderItem(24L, 15), createOrderItem(13L, 7), createOrderItem(8L, 2));

        log.info("Loaded eShop orders.");
    }
}
