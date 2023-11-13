package cz.muni.fi.orderService.controller;

import cz.fi.muni.pa165.dto.OrderDTO;
import cz.fi.muni.pa165.enums.OrderState;
import cz.fi.muni.pa165.facade.OrderFacade;
import cz.fi.muni.pa165.rest.ApiUris;
import cz.fi.muni.pa165.rest.exceptions.InvalidParameterException;
import cz.fi.muni.pa165.rest.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * REST Controller for Orders
 * 
 * @author brossi
 */
@RestController
@RequestMapping(ApiUris.ROOT_URI_ORDERS)
public class OrderController {
    
    final static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Inject
    private OrderFacade orderFacade;

    /**
     *
     * Getting all the orders according to the given parameters
     * 
     * @param status 
     * @param lastWeek true if considering only orders from last week
     * @return list of OrderDTOs
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<OrderDTO> getOrders(@RequestParam("status") String status,
            @RequestParam(value = "last_week", required = false, defaultValue = "false") boolean lastWeek) {
        
        logger.debug("rest getOrders({},{})", lastWeek, status);


        if (status.equalsIgnoreCase("ALL")) {
            return orderFacade.getAllOrders();
        }

        if (!OrderState.contains(status)) {
            throw new InvalidParameterException();
        }

        final OrderState os = OrderState.valueOf(status);

        if (lastWeek) {
            return orderFacade.getAllOrdersLastWeek(os);
        } else {
            return orderFacade.getOrdersByState(os);
        }
    }

    /**
     * 
     * @param userId
     * @return 
     */
    @RequestMapping(value = "by_user_id/{user_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<OrderDTO> getOrdersByUserId(@PathVariable("user_id") long userId) {
        
        logger.debug("rest getOrderByUserId({})", userId);

            List<OrderDTO> orderDTOs = orderFacade.getOrdersByUser(userId);
            if (orderDTOs == null){
                    throw new ResourceNotFoundException();
            }       
            return orderDTOs;

    }

    /**
     * 
     * @param id
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final OrderDTO getOrder(@PathVariable("id") long id) throws Exception {
       
        logger.debug("rest getOrder({})", id);

        OrderDTO orderDTO = orderFacade.getOrderById(id);
        if (orderDTO == null) {
            throw new ResourceNotFoundException();
        }

        return orderDTO;
    
    }

    /**
     * Perform one action on the order
     * Either cancelling, shipping or finishing the order
     * @param orderId
     * @param action one of CANCEL, SHIP, FINISH
     * @return 
     */
    @RequestMapping(value = "{order_id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public final OrderDTO shipOrder(@PathVariable("order_id") long orderId, @RequestParam("action") String action) {
        
        logger.debug("rest shipOrder({})", orderId);

        if (action.equalsIgnoreCase("CANCEL")) {
            orderFacade.cancelOrder(orderId);
        } else if (action.equalsIgnoreCase("SHIP")) {
            orderFacade.shipOrder(orderId);
        } else if (action.equalsIgnoreCase("FINISH")) {
            orderFacade.finishOrder(orderId);
        } else {
            throw new InvalidParameterException();
        }

        return orderFacade.getOrderById(orderId);
    }

}
