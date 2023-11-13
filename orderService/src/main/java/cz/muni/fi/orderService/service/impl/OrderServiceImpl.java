package cz.muni.fi.orderService.service.impl;

import cz.fi.muni.pa165.dao.OrderDao;
import cz.fi.muni.pa165.dao.OrderItemDao;
import cz.fi.muni.pa165.entity.Order;
import cz.fi.muni.pa165.entity.OrderItem;
import cz.fi.muni.pa165.entity.Price;
import cz.fi.muni.pa165.entity.User;
import cz.fi.muni.pa165.enums.Currency;
import cz.fi.muni.pa165.enums.OrderState;
import cz.fi.muni.pa165.exceptions.EshopServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Implementation of the {@link OrderService}. This class is part of the service
 * module of the application that provides the implementation of the business
 * logic (main logic of the application).
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
	private OrderDao orderDao;
	@Autowired
	private TimeService timeService;
    @Autowired
    private ExchangeService exchangeService;

    @Override
    public void createOrder(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemDao.create(orderItem);
        }
        orderDao.create(order);
    }

    @Override
	public Order findOrderById(Long id) {
		return orderDao.findById(id);
	}

	@Override
	public Price getTotalPrice(long orderId, Currency currency) {
		Order order = this.findOrderById(orderId);
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            BigDecimal itemPrice = item.getPricePerItem().getValue().multiply(new BigDecimal(item.getAmount()));
            Currency itemCurrency = item.getPricePerItem().getCurrency();
            if(itemCurrency != currency) {
                itemPrice = itemPrice.multiply(exchangeService.getCurrencyRate(itemCurrency, currency));
            }
            totalPrice = totalPrice.add(itemPrice);
        }
        Price price = new Price();
        price.setCurrency(currency);
        price.setValue(totalPrice);
        return price;
    }

	@Override
	public List<Order> getOrdersByState(OrderState state) {
		return orderDao.getOrdersWithState(state);
	}

	@Override
	public List<Order> findAllOrders() {
		return orderDao.findAll();
	}

	@Override
	public List<Order> getOrdersByUser(User user) {
		return orderDao.findByUser(user);
	}

	@Override
	public List<Order> getAllOrdersLastWeek(OrderState state) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timeService.getCurrentTime());
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date lastWeek = calendar.getTime();
		List<Order> orders = orderDao.getOrdersCreatedBetween(lastWeek, timeService.getCurrentTime(),
				state);
		return orders;
	}

	/**
	 * The only allowed changes of state are: RECIEVED - CANCELED RECEIVED -
	 * SHIPPED SHIPPED - DONE
	 */
	private Set<Transition> allowedTransitions = new HashSet<Transition>();
	{
		allowedTransitions.add(new Transition(OrderState.RECEIVED,
				OrderState.SHIPPED));
		allowedTransitions.add(new Transition(OrderState.RECEIVED,
				OrderState.CANCELED));
		allowedTransitions.add(new Transition(OrderState.SHIPPED,
				OrderState.DONE));
	}

	@Override
	public void shipOrder(Order order) {
		checkTransition(order.getState(), OrderState.SHIPPED);
		order.setState(OrderState.SHIPPED);
	}

	@Override
	public void finishOrder(Order order) {
		checkTransition(order.getState(), OrderState.DONE);
		order.setState(OrderState.DONE);

	}

	@Override
	public void cancelOrder(Order order) {
		checkTransition(order.getState(), OrderState.CANCELED);
		order.setState(OrderState.CANCELED);
	}

	private void checkTransition(OrderState oldState, OrderState newState) {
		if (!allowedTransitions.contains(new Transition(oldState, newState)))
			throw new EshopServiceException("The transition from: " + oldState
					+ " to " + newState + " is not allowed!");

	}

}
