package cz.muni.fi.orderService.service.impl;

import cz.muni.fi.orderService.dto.OrderDTO;
import cz.muni.fi.orderService.dto.OrderItemDTO;
import cz.muni.fi.orderService.dto.ProductDTO;
import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.entity.OrderItem;
import cz.muni.fi.orderService.enums.OrderState;
import cz.muni.fi.orderService.exception.EshopServiceException;
import cz.muni.fi.orderService.feign.ProductInterface;
import cz.muni.fi.orderService.feign.UserInterface;
import cz.muni.fi.orderService.repository.OrderItemRepository;
import cz.muni.fi.orderService.repository.OrderRepository;
import cz.muni.fi.orderService.service.BeanMappingService;
import cz.muni.fi.orderService.service.OrderService;
import cz.muni.fi.orderService.utils.Transition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserInterface userInterface;

	@Autowired
	private ProductInterface productInterface;

	@Autowired
	private BeanMappingService beanMappingService;

    @Override
    public void createOrder(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemRepository.save(orderItem);
        }
        orderRepository.save(order);
    }

	@Override
	public List<Order> getAllOrdersLastWeek(OrderState state) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date lastWeek = calendar.getTime();

        return orderRepository.getOrdersCreatedBetween(lastWeek, new Date(), state);
	}

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
	public void shipOrder(Order order, OrderState state) {
		checkTransition(order.getState(), state);
		order.setState(state);
		orderRepository.save(order);
	}

	@Override
	public OrderDTO getOrderDTOFromOrder(Order order) {
		OrderDTO orderDTO = beanMappingService.mapTo(order, OrderDTO.class);
		orderDTO.setUser(userInterface.getUser(order.getUserId()).getBody());
		List<OrderItemDTO> orderItemDTOs = new ArrayList<>();

		for (OrderItem orderItem : order.getOrderItems()) {
			OrderItemDTO orderItemDTO = beanMappingService.mapTo(orderItem, OrderItemDTO.class);
			ProductDTO productDTO = productInterface.getProduct(orderItem.getProductId()).getBody();
			orderItemDTO.setProduct(productDTO);
			orderItemDTOs.add(orderItemDTO);
		}

		orderDTO.setOrderItems(orderItemDTOs);
		return orderDTO;
	}

	private void checkTransition(OrderState oldState, OrderState newState) {
		if (!allowedTransitions.contains(new Transition(oldState, newState)))
			throw new EshopServiceException("The transition from: " + oldState
					+ " to " + newState + " is not allowed!");
	}
}
