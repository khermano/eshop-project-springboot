package cz.muni.fi.orderService.service;

import cz.fi.muni.pa165.entity.Order;
import cz.fi.muni.pa165.entity.Price;
import cz.fi.muni.pa165.entity.User;
import cz.fi.muni.pa165.enums.Currency;
import cz.fi.muni.pa165.enums.OrderState;

import java.util.List;

/**
 * An interface that defines a service access to the {@link Order} entity.
 */
public interface OrderService {

	void createOrder(Order order);

	/**
	 * Get all saved orders belonging to the given user.
	 */
	List<Order> getOrdersByUser(User user);
	
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

	Price getTotalPrice(long orderId, Currency currency);
}
