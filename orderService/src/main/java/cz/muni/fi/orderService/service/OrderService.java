package cz.muni.fi.orderService.service;

import cz.muni.fi.orderService.dto.OrderDTO;
import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.OrderState;
import java.util.List;

/**
 * An interface that defines service access to the {@link Order} entity.
 */
public interface OrderService {
	void createOrder(Order order);
	
	/**
	 * Get all orders saved within last week that have the given state.
	 */
	List<Order> getAllOrdersLastWeek(OrderState state);

	void shipOrder(Order order, OrderState state);

	OrderDTO getOrderDTOFromOrder(Order order);
}
