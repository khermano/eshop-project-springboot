package cz.muni.fi.orderService.dto;

import cz.muni.fi.orderService.enums.Color;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ProductDTO {
    private Long id;

    private String name;

    private String description;

    private Color color;

    private Date addedDate;

    private Set<CategoryDTO> categories = new HashSet<>();

    private List<PriceDTO> priceHistory = new ArrayList<>();

    private PriceDTO currentPrice;
}
