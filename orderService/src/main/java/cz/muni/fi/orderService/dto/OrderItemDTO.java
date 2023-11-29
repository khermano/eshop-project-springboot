package cz.muni.fi.orderService.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;

    private ProductDTO product;

    private Integer amount;
}
