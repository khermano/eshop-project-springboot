package cz.muni.fi.orderService.controller;

import cz.muni.fi.orderService.dto.OrderDTO;
import cz.muni.fi.orderService.dto.UserDTO;
import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.OrderState;
import cz.muni.fi.orderService.feign.ProductInterface;
import cz.muni.fi.orderService.feign.UserInterface;
import cz.muni.fi.orderService.repository.OrderRepository;
import cz.muni.fi.orderService.service.BeanMappingService;
import cz.muni.fi.orderService.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    @Mock
    private UserInterface userInterface;

    @Mock
    private ProductInterface productInterface;

    @Mock
    private BeanMappingService beanMappingService;

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
        List<Order> orders = Arrays.asList(createOrders().get(0), createOrders().get(1), createOrders().get(5));
        doReturn(orders).when(orderRepository).findAll();
        doReturn(getMockedOrderDTOList().get(0)).when(orderService).getOrderDTOFromOrder(orders.get(0));
        doReturn(getMockedOrderDTOList().get(1)).when(orderService).getOrderDTOFromOrder(orders.get(1));
        doReturn(getMockedOrderDTOList().get(5)).when(orderService).getOrderDTOFromOrder(orders.get(2));

        mockMvc.perform(get("/").param("status", "ALL"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==1)].state").value("DONE"))
                .andExpect(jsonPath("$.[?(@.id==2)].state").value("CANCELED"))
                .andExpect(jsonPath("$.[?(@.id==6)].state").value("SHIPPED"));
    }

    @Test
    public void getAllOrdersByState() throws Exception {
        List<Order> orders = Arrays.asList(createOrders().get(0), createOrders().get(5));

        doReturn(orders).when(orderRepository).findByState(OrderState.DONE);
        doReturn(getMockedOrderDTOList().get(0)).when(orderService).getOrderDTOFromOrder(orders.get(0));
        doReturn(getMockedOrderDTOList().get(5)).when(orderService).getOrderDTOFromOrder(orders.get(1));

        mockMvc.perform(get("/").param("status", "DONE"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==1)].state").value("DONE"));
    }

    @Test
    public void getAllOrdersByUserId() throws Exception {
        List<Order> orders = Arrays.asList(createOrders().get(0), createOrders().get(1), createOrders().get(5));

        doReturn(new ResponseEntity<>(new UserDTO(), HttpStatus.OK)).when(userInterface).getUser(1L);
        doReturn(Collections.unmodifiableList(orders)).when(orderRepository).findByUserId(1L);
        doReturn(getMockedOrderDTOList().get(0)).when(orderService).getOrderDTOFromOrder(orders.get(0));
        doReturn(getMockedOrderDTOList().get(1)).when(orderService).getOrderDTOFromOrder(orders.get(1));
        doReturn(getMockedOrderDTOList().get(5)).when(orderService).getOrderDTOFromOrder(orders.get(2));

        mockMvc.perform(get("/by_user_id/1"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==1)].state").value("DONE"))
                .andExpect(jsonPath("$.[?(@.id==2)].state").value("CANCELED"))
                .andExpect(jsonPath("$.[?(@.id==6)].state").value("SHIPPED"));
    }

    @Test
    public void getValidOrder() throws Exception {
        List<Order> orders = Arrays.asList(createOrders().get(0), createOrders().get(1), createOrders().get(5));

        doReturn(Optional.of(orders.get(0))).when(orderRepository).findById(1L);
        doReturn(Optional.of(orders.get(1))).when(orderRepository).findById(2L);
        doReturn(Optional.of(orders.get(2))).when(orderRepository).findById(6L);
        doReturn(getMockedOrderDTOList().get(0)).when(orderService).getOrderDTOFromOrder(orders.get(0));
        doReturn(getMockedOrderDTOList().get(1)).when(orderService).getOrderDTOFromOrder(orders.get(1));
        doReturn(getMockedOrderDTOList().get(5)).when(orderService).getOrderDTOFromOrder(orders.get(2));

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.state").value("DONE"));

        mockMvc.perform(get("/2"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.state").value("CANCELED"));

        mockMvc.perform(get("/6"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.state").value("SHIPPED"));
    }

     @Test
    public void getInvalidOrder() throws Exception {
        doReturn(Optional.empty()).when(orderRepository).findById(1L);

        mockMvc.perform(get("/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shipOrder() throws Exception {
        doReturn(Optional.of(createOrders().get(0))).when(orderRepository).findById(1L);
        doNothing().when(orderService).shipOrder(any(Order.class));
        doReturn(getMockedOrderDTOList().get(0)).when(orderService).getOrderDTOFromOrder(any(Order.class));

        mockMvc.perform(post("/1").param("action", "SHIP"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void shipOrderInvalidAction() throws Exception {
        doReturn(Optional.of(createOrders().get(0))).when(orderRepository).findById(1L);

        mockMvc.perform(post("/1").param("action", "INVALID"))
                .andExpect(status().isNotAcceptable());
    }


    private List<Order> createOrders() {
        Order orderOne = new Order();
        orderOne.setId(1L);
        orderOne.setState(OrderState.DONE);
        orderOne.setCreated(Calendar.getInstance().getTime());
        orderOne.setUserId(1L);

        Order orderTwo = new Order();
        orderTwo.setId(2L);
        orderTwo.setState(OrderState.CANCELED);
        orderTwo.setCreated(Calendar.getInstance().getTime());
        orderTwo.setUserId(1L);


        Order orderThree = new Order();
        orderThree.setId(3L);
        orderThree.setState(OrderState.RECEIVED);
        orderThree.setCreated(Calendar.getInstance().getTime());
        orderThree.setUserId(1L);

        Order orderFour = new Order();
        orderFour.setId(4L);
        orderFour.setState(OrderState.SHIPPED);
        orderFour.setCreated(Calendar.getInstance().getTime());
        orderFour.setUserId(1L);


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10);

        Order orderFive = new Order();
        orderFive.setId(5L);
        orderFive.setState(OrderState.DONE);
        orderFive.setCreated(cal.getTime());
        orderFive.setUserId(1L);

        Order orderSix = new Order();
        orderSix.setId(6L);
        orderSix.setState(OrderState.SHIPPED);
        orderSix.setCreated(cal.getTime());
        orderSix.setUserId(1L);

        return Arrays.asList(orderOne, orderTwo, orderThree, orderFour, orderFive, orderSix);
    }

    private List<OrderDTO> getMockedOrderDTOList() {
        OrderDTO mockedOrderDTO = new OrderDTO();
        mockedOrderDTO.setId(1L);
        mockedOrderDTO.setState(OrderState.DONE);

        OrderDTO mockedOrderDTO2 = new OrderDTO();
        mockedOrderDTO2.setId(2L);
        mockedOrderDTO2.setState(OrderState.CANCELED);

        OrderDTO mockedOrderDTO3 = new OrderDTO();
        mockedOrderDTO3.setId(3L);
        mockedOrderDTO3.setState(OrderState.CANCELED);

        OrderDTO mockedOrderDTO4 = new OrderDTO();
        mockedOrderDTO4.setId(4L);
        mockedOrderDTO4.setState(OrderState.CANCELED);

        OrderDTO mockedOrderDTO5 = new OrderDTO();
        mockedOrderDTO5.setId(5L);
        mockedOrderDTO5.setState(OrderState.CANCELED);

        OrderDTO mockedOrderDTO6 = new OrderDTO();
        mockedOrderDTO6.setId(6L);
        mockedOrderDTO6.setState(OrderState.SHIPPED);

        return Arrays.asList(mockedOrderDTO, mockedOrderDTO2, mockedOrderDTO3, mockedOrderDTO4, mockedOrderDTO5, mockedOrderDTO6);
    }
}
