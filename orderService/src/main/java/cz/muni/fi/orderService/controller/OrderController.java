package cz.muni.fi.orderService.controller;

import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.OrderState;
import cz.muni.fi.orderService.exception.InvalidParameterException;
import cz.muni.fi.orderService.exception.ResourceNotFoundException;
import cz.muni.fi.orderService.repository.OrderRepository;
import cz.muni.fi.orderService.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Orders
 *
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    final static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    /**
     *
     * Getting all the orders according to the given parameters
     * 
     * @param status 
     * @param lastWeek true if considering only orders from last week
     * @return list of OrderDTOs
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<Order> getOrders(@RequestParam("status") String status,
                                       @RequestParam(value = "last_week", required = false, defaultValue = "false") boolean lastWeek) {
        logger.debug("rest getOrders({},{})", lastWeek, status);


        if (status.equalsIgnoreCase("ALL")) {
            return orderRepository.findAll();
        }

        if (!OrderState.contains(status)) {
            throw new InvalidParameterException();
        }

        final OrderState os = OrderState.valueOf(status);

        if (lastWeek) {
            return orderService.getAllOrdersLastWeek(os);
        } else {
            return orderRepository.findByState(os);
        }
    }

    /**
     * 
     * @param userId
     * @return
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value = "by_user_id/{user_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<Order> getOrdersByUserId(@PathVariable("user_id") long userId) throws ResourceNotFoundException {
        logger.debug("rest getOrderByUserId({})", userId);

            List<Order> orders = orderRepository.findByUserId(userId);
            if (orders == null){
                    throw new ResourceNotFoundException();
            }       
            return orders;

    }

    /**
     * 
     * @param id
     * @return
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final Order getOrder(@PathVariable("id") long id) throws ResourceNotFoundException {
        logger.debug("rest getOrder({})", id);

        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Perform one action on the order
     * Either cancelling, shipping or finishing the order
     * @param orderId
     * @param action one of CANCEL, SHIP, FINISH
     * @return
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value = "{order_id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public final Order shipOrder(@PathVariable("order_id") long orderId, @RequestParam("action") String action) throws  ResourceNotFoundException{
        logger.debug("rest shipOrder({})", orderId);

        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        if (action.equalsIgnoreCase("CANCEL")) {
            orderService.cancelOrder(order.get());
        } else if (action.equalsIgnoreCase("SHIP")) {
            orderService.shipOrder(order.get());
        } else if (action.equalsIgnoreCase("FINISH")) {
            orderService.finishOrder(order.get());
        } else {
            throw new InvalidParameterException();
        }

        order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
