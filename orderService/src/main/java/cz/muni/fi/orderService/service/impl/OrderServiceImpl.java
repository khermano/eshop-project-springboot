package cz.muni.fi.orderService.service.impl;

import cz.muni.fi.orderService.dto.PriceDTO;
import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.entity.OrderItem;
import cz.muni.fi.orderService.enums.Currency;
import cz.muni.fi.orderService.enums.OrderState;
import cz.muni.fi.orderService.exception.EshopServiceException;
import cz.muni.fi.orderService.repository.OrderItemRepository;
import cz.muni.fi.orderService.repository.OrderRepository;
import cz.muni.fi.orderService.service.OrderService;
import cz.muni.fi.orderService.utils.Transition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of the {@link OrderService}. This class is part of the service
 * module of the application that provides the implementation of the business
 * logic (main logic of the application).
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
	private OrderRepository orderRepository;

    @Override
    public void createOrder(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemRepository.save(orderItem);
        }
        orderRepository.save(order);
    }

	@Override
	public PriceDTO getTotalPrice(long orderId, Currency currency) {
		Optional<Order> order = orderRepository.findById(orderId);
		if (order.isEmpty()) {
			throw new EshopServiceException("Order with given id does not exist"); //TODO toto je pridane, skontroluj ci to niekede vyhadzuje
		}
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : order.get().getOrderItems()) {
			PriceDTO pricePerItem = null; //TODO get product price
            BigDecimal itemPrice = pricePerItem.getValue().multiply(new BigDecimal(item.getAmount()));
            Currency itemCurrency = pricePerItem.getCurrency();
            if(itemCurrency != currency) {
				BigDecimal currencyRate = null; //TODO getCurrencyRate(itemCurrency, currency)
                itemPrice = itemPrice.multiply(currencyRate);
            }
            totalPrice = totalPrice.add(itemPrice);
        }
        PriceDTO price = new PriceDTO();
        price.setCurrency(currency);
        price.setValue(totalPrice);
        return price;
    }

	@Override
	public List<Order> getAllOrdersLastWeek(OrderState state) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date lastWeek = calendar.getTime();
        return orderRepository.getOrdersCreatedBetween(lastWeek, new Date(),
				state);
	}

	/**
	 * The only allowed changes of state are: RECEIVED - CANCELED,
	 * RECEIVED - SHIPPED, SHIPPED - DONE
	 */
	private final Set<Transition> allowedTransitions = new HashSet<>();
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
