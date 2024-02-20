package cz.muni.fi.productService.dto;

import cz.muni.fi.productService.enums.Color;
import cz.muni.fi.productService.enums.Currency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductCreateDTO {
    private byte[] image;

    private String imageMimeType;

    @NotNull
    private String name;

    private String description;

    private Color color;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotNull
    private Currency currency;

    @NotNull
    private Long categoryId;
}
