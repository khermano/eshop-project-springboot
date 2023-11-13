package cz.muni.fi.orderService.service;

import cz.muni.fi.orderService.dto.PriceDTO;
import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.Currency;
import cz.muni.fi.orderService.enums.OrderState;
import java.io.IOException;
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

	void shipOrder(Order order);

	void finishOrder(Order order);

	void cancelOrder(Order order);

	PriceDTO getTotalPrice(long orderId, Currency currency) throws IOException;
}
