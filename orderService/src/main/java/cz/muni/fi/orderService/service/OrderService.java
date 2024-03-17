package cz.muni.fi.orderService.service;

import cz.muni.fi.orderService.dto.OrderDTO;
import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.OrderState;
import java.util.List;

public interface OrderService {
	void createOrder(Order order);

	List<Order> getAllOrdersLastWeek(OrderState state);

	void shipOrder(Order order, OrderState state);

	OrderDTO getOrderDTOFromOrder(Order order);
}
