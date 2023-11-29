package cz.muni.fi.orderService.dto;

import cz.muni.fi.orderService.enums.OrderState;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;

    private UserDTO user;

    private List<OrderItemDTO> orderItems = new ArrayList<>();

    private Date created;

    private OrderState state;
}
