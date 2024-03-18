package cz.muni.fi.productService.dto;

import cz.muni.fi.productService.enums.Color;
import cz.muni.fi.productService.enums.Currency;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductCreateDTO {
    private byte[] image;

    private String imageMimeType;

    private String name;

    private String description;

    private Color color;

    private BigDecimal price;

    private Currency currency;

    private Long categoryId;
}
