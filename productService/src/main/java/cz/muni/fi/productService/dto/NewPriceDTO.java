package cz.muni.fi.productService.dto;

import cz.muni.fi.productService.enums.Currency;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class NewPriceDTO {
    private BigDecimal value;

    private Currency currency;
}
