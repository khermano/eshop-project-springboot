package cz.muni.fi.orderService.service;

import cz.muni.fi.orderService.dto.PriceDTO;
import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.Currency;
import cz.muni.fi.orderService.enums.OrderState;
import java.util.List;

/**
 * An interface that defines service access to the {@link Order} entity.
 */
public interface OrderService {
	void createOrder(Order order);

	/**
	 * Get all saved orders belonging to the given user.
	 */
	List<Order> getOrdersByUserId(Long userId);
	
	/**
	 * Get all orders saved within last week that have the given state.
	 */
	List<Order> getAllOrdersLastWeek(OrderState state);

	/**
	 * Get all orders with the given state.
	 */
	List<Order> getOrdersByState(OrderState state);

	List<Order> findAllOrders();

	void shipOrder(Order order);

	void finishOrder(Order order);

	void cancelOrder(Order order);

	Order findOrderById(Long id);

	PriceDTO getTotalPrice(long orderId, Currency currency);
}
