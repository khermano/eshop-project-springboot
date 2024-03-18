package cz.muni.fi.productService.dto;

import cz.muni.fi.productService.enums.Currency;
import java.math.BigDecimal;

public class NewPriceDTO {
    private BigDecimal value;

    private Currency currency;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
