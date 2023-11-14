package cz.muni.fi.orderService.controller;

import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.OrderState;
import cz.muni.fi.orderService.repository.OrderRepository;
import cz.muni.fi.orderService.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(orderController).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void getAllOrders() throws Exception {
        doReturn(Collections.unmodifiableList(this.createOrders())).when(orderRepository).findAll();

        mockMvc.perform(get("/orders").param("status", "ALL"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==1)].state").value("DONE"))
                .andExpect(jsonPath("$.[?(@.id==2)].state").value("CANCELED"))
                .andExpect(jsonPath("$.[?(@.id==6)].state").value("SHIPPED"));
    }

//    @Test
//    public void getAllOrdersByState() throws Exception {
//        List<Order> orders = Arrays.asList(this.createOrders().get(0).get(), this.createOrders().get(5).get());
//        //List<Order> orders = new ArrayList<>(this.createOrders().get(0).get());
//        /*this.createOrders().stream()
//           .filter(o ->
//               o.get().getState() == OrderState.DONE
//           ).forEach(o -> orders.add(o.get()));*/
//
//        doReturn(orders).when(orderRepository).findByState(OrderState.DONE);
//
//        mockMvc.perform(get("/orders").param("status", "ALL"))
//                .andExpect(status().isOk())
//                .andExpect(
//                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.[?(@.id==1)].state").value("DONE"));
//                //.andExpect(jsonPath("$.[?(@.id==5)].state").value("DONE"));
//    }

//    @Test
//    public void getAllOrdersByUserId() throws Exception {
//        doReturn(Collections.unmodifiableList(this.createOrders())).when(orderRepository).findByUserId(1L);
//
//        mockMvc.perform(get("/orders/by_user_id/1"))
//                .andExpect(status().isOk())
//                .andExpect(
//                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.[?(@.id==1)].state").value("DONE"))
//                .andExpect(jsonPath("$.[?(@.id==2)].state").value("CANCELED"))
//                .andExpect(jsonPath("$.[?(@.id==6)].state").value("SHIPPED"));
//    }

    @Test
    public void getValidOrder() throws Exception {
        List<Optional<Order>> orders = this.createOrders();

        doReturn(orders.get(0)).when(orderRepository).findById(1L);
        doReturn(orders.get(1)).when(orderRepository).findById(2L);
        doReturn(orders.get(5)).when(orderRepository).findById(6L);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.state").value("DONE"));

        mockMvc.perform(get("/orders/2"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.state").value("CANCELED"));

        mockMvc.perform(get("/orders/6"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.state").value("SHIPPED"));
    }

     @Test
    public void getInvalidOrder() throws Exception {
        doReturn(Optional.empty()).when(orderRepository).findById(1L);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shipOrder() throws Exception {
        List<Optional<Order>> orders = this.createOrders();

        doReturn(orders.get(0)).when(orderRepository).findById(1L);
        doNothing().when(orderService).shipOrder(any(Order.class));

        mockMvc.perform(post("/orders/1").param("action", "SHIP"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void shipOrderInvalidAction() throws Exception {
        List<Optional<Order>> orders = this.createOrders();

        doReturn(orders.get(0)).when(orderRepository).findById(1L);

        mockMvc.perform(post("/orders/1").param("action", "INVALID"))
                .andExpect(status().isNotAcceptable());
    }


    private List<Optional<Order>> createOrders() {
        Order orderOne = new Order();
        orderOne.setId(1L);
        orderOne.setState(OrderState.DONE);
        orderOne.setCreated(Calendar.getInstance().getTime());

        Order orderTwo = new Order();
        orderTwo.setId(2L);
        orderTwo.setState(OrderState.CANCELED);
        orderTwo.setCreated(Calendar.getInstance().getTime());


        Order orderThree = new Order();
        orderThree.setId(3L);
        orderThree.setState(OrderState.RECEIVED);
        orderThree.setCreated(Calendar.getInstance().getTime());

        Order orderFour = new Order();
        orderFour.setId(4L);
        orderFour.setState(OrderState.SHIPPED);
        orderFour.setCreated(Calendar.getInstance().getTime());


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10);

        Order orderFive = new Order();
        orderFive.setId(5L);
        orderFive.setState(OrderState.DONE);
        orderFive.setCreated(cal.getTime());

        Order orderSix = new Order();
        orderSix.setId(6L);
        orderSix.setState(OrderState.SHIPPED);
        orderSix.setCreated(cal.getTime());

        return Arrays.asList(Optional.of(orderOne), Optional.of(orderTwo), Optional.of(orderThree),
                Optional.of(orderFour), Optional.of(orderFive), Optional.of(orderSix));
    }
}
