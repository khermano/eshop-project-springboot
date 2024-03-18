package cz.muni.fi.orderService.dto;

import cz.muni.fi.orderService.enums.OrderState;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDTO {
    private Long id;

    private UserDTO user;

    private List<OrderItemDTO> orderItems = new ArrayList<>();

    private Date created;

    private OrderState state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }
}
