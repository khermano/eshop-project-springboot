package cz.muni.fi.orderService.controller;

import cz.muni.fi.orderService.dto.OrderDTO;
import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.OrderState;
import cz.muni.fi.orderService.feign.UserInterface;
import cz.muni.fi.orderService.repository.OrderRepository;
import cz.muni.fi.orderService.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Orders
 *
 */
@RestController
public class OrderController {
    final static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserInterface userInterface;


    /**
     * Getting all the orders according to the given parameters
     *
     * curl -i -X GET
     * http://localhost:8084?status=ALL
     * and
     * curl -i -X GET
     * http://localhost:8084?status=ALL&last_week=TRUE
     * 
     * @param status can be {ALL, RECEIVED, CANCELED, SHIPPED, DONE}
     *               defines orders with StateOrder (RECEIVED, CANCELED, SHIPPED, DONE) or ALL orders
     * @param lastWeek if true we consider only orders from last week
     * @return list of Orders by given parameters
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam("status") String status,
                                                      @RequestParam(value = "last_week", required = false, defaultValue = "false")
                                       boolean lastWeek) {
        logger.debug("rest getOrders({},{})", lastWeek, status);

        if (status.equalsIgnoreCase("ALL")) {
            List<OrderDTO> orderDTOs = new ArrayList<>();
            for (Order order: orderRepository.findAll()) {
                orderDTOs.add(orderService.getOrderDTOFromOrder(order));
            }
            return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
        }

        if (!OrderState.contains(status)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

        final OrderState os = OrderState.valueOf(status);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        if (lastWeek) {
            for (Order order: orderService.getAllOrdersLastWeek(os)) {
                orderDTOs.add(orderService.getOrderDTOFromOrder(order));
            }
        } else {
            for (Order order: orderRepository.findByState(os)) {
                orderDTOs.add(orderService.getOrderDTOFromOrder(order));
            }
        }
        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    /**
     * Getting all the orders created by user given by the ID
     * Be aware that userService must be running!
     *
     * curl -i -X GET
     * http://localhost:8084/by_user_id/1
     *
     * @param userId ID of user who created orders
     * @return list of Orders by given parameter
     */
    @GetMapping(value = "by_user_id/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable("user_id") long userId) {
        logger.debug("rest getOrderByUserId({})", userId);

        List<OrderDTO> orderDTOs = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        try {
            if (userInterface.getUser(userId).getStatusCode() == HttpStatusCode.valueOf(200)) {
                orders = orderRepository.findByUserId(userId);
            }
        } catch (Exception e) {
            // we needed to add to reproduce behaviour of the original project
        }

        if (orders == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested resource was not found");
        }
        for (Order order : orders) {
            orderDTOs.add(orderService.getOrderDTOFromOrder(order));
        }

        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    /**
     * Get Order by identifier id
     * curl -i -X GET
     * http://localhost:8084/1
     *
     * @param id identifier for an order
     * @return Order with given id
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("id") long id) {
        logger.debug("rest getOrder({})", id);

        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return new ResponseEntity<>(orderService.getOrderDTOFromOrder(order.get()), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested resource was not found");
        }
    }

    /**
     * Perform one action on the order
     * Either cancelling, shipping or finishing the order
     * The only allowed changes of state are: RECEIVED -> CANCELED (action=CANCEL),
     * RECEIVED -> SHIPPED (action=SHIP), SHIPPED -> DONE (action=FINISH)
     *
     * curl -i -X POST
     * http://localhost:8084/2?action=FINISH
     *
     * @param orderId identifier for an order
     * @param action one of CANCEL, SHIP, FINISH
     * @return Order on which action was performed
     */
    @PostMapping(value = "{order_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> shipOrder(@PathVariable("order_id") long orderId, @RequestParam("action") String action) {
        logger.debug("rest shipOrder({})", orderId);

        Optional<Order> order = orderRepository.findById(orderId);
        //I have to give up the check to get same behaviour as in the original project
        /*if (order.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }*/

        if (action.equalsIgnoreCase("CANCEL")) {
            orderService.shipOrder(order.get(), OrderState.CANCELED);
        } else if (action.equalsIgnoreCase("SHIP")) {
            orderService.shipOrder(order.get(), OrderState.SHIPPED);
        } else if (action.equalsIgnoreCase("FINISH")) {
            orderService.shipOrder(order.get(), OrderState.DONE);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

        order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return new ResponseEntity<>(orderService.getOrderDTOFromOrder(order.get()), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
