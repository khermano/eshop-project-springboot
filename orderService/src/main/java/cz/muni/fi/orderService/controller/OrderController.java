package cz.muni.fi.orderService.controller;

import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.OrderState;
import cz.muni.fi.orderService.exception.InvalidParameterException;
import cz.muni.fi.orderService.exception.ResourceNotFoundException;
import cz.muni.fi.orderService.repository.OrderRepository;
import cz.muni.fi.orderService.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
     * Getting all the orders according to the given parameters
     *
     * curl -i -X GET
     * http://localhost:8084/eshop-rest/orders?status=ALL
     * and
     * curl -i -X GET
     * http://localhost:8084/eshop-rest/orders?status=ALL&last_week=TRUE)
     * 
     * @param status can be {ALL, RECEIVED, CANCELED, SHIPPED, DONE}
     *               defines orders with StateOrder (RECEIVED, CANCELED, SHIPPED, DONE) or ALL orders
     * @param lastWeek if true we consider only orders from last week
     * @return list of Orders by given parameters
     * @throws InvalidParameterException if invalid status provided
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<List<Order>> getOrders(@RequestParam("status") String status,
                                                      @RequestParam(value = "last_week", required = false, defaultValue = "false")
                                       boolean lastWeek) throws InvalidParameterException {
        logger.debug("rest getOrders({},{})", lastWeek, status);

        if (status.equalsIgnoreCase("ALL")) {
            return new ResponseEntity<>(orderRepository.findAll(), HttpStatus.OK);
        }

        if (!OrderState.contains(status)) {
            throw new InvalidParameterException();
        }

        final OrderState os = OrderState.valueOf(status);

        if (lastWeek) {
            return new ResponseEntity<>(orderService.getAllOrdersLastWeek(os), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(orderRepository.findByState(os), HttpStatus.OK);
        }
    }

    /**
     * Getting all the orders created by user given by the ID
     * Be aware that userService must be running!
     *
     * curl -i -X GET
     * http://localhost:8084/eshop-rest/orders/by_user_id/1
     *
     * @param userId ID of user who created orders
     * @return list of Orders by given parameter
     * @throws ResourceNotFoundException if userService not returns user by given ID, or we can't get orders from DB
     * @throws IOException when there is an error trying to call userService
     */
    @GetMapping(value = "by_user_id/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable("user_id") long userId) throws ResourceNotFoundException, IOException {
        logger.debug("rest getOrderByUserId({})", userId);

        URL url = new URL("http://localhost:8081/eshop-rest/users/" + userId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(HttpMethod.GET.name());
        if (con.getResponseCode() != HttpServletResponse.SC_OK) {
            con.disconnect();
            throw new ResourceNotFoundException();
        }
        con.disconnect();

        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders == null){
            throw new ResourceNotFoundException();
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Get Order by identifier id
     * curl -i -X GET
     * http://localhost:8084/eshop-rest/orders/1
     *
     * @param id identifier for an order
     * @return Order with given id
     * @throws ResourceNotFoundException if order with given id does not exist
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Order> getOrder(@PathVariable("id") long id) throws ResourceNotFoundException {
        logger.debug("rest getOrder({})", id);

        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Perform one action on the order
     * Either cancelling, shipping or finishing the order
     * The only allowed changes of state are: RECEIVED -> CANCELED (action=CANCEL),
     * RECEIVED -> SHIPPED (action=SHIP), SHIPPED -> DONE (action=FINISH)
     *
     * curl -i -X POST
     * http://localhost:8084/eshop-rest/orders/2?action=FINISH
     *
     * @param orderId identifier for an order
     * @param action one of CANCEL, SHIP, FINISH
     * @return Order on which action was performed
     * @throws ResourceNotFoundException if we can get order with given id before or after action
     * @throws InvalidParameterException if invalid action provided
     */
    @PostMapping(value = "{order_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Order> shipOrder(@PathVariable("order_id") long orderId, @RequestParam("action") String action)
            throws ResourceNotFoundException, InvalidParameterException {
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
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
