package cz.muni.fi.productService.dto;

import cz.muni.fi.productService.enums.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class NewPriceDTO {
    private Long productId;

    @NotNull
    private BigDecimal value;

    @NotNull
    private Currency currency;
}
