package cz.muni.fi.orderService.dto;

import cz.muni.fi.orderService.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDTO {
	private Long id;
	private BigDecimal value;
	private Currency currency;
	private Date priceStart;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceDTO priceDTO = (PriceDTO) o;

        if (value != null ? !value.equals(priceDTO.value) : priceDTO.value != null) return false;
        if (currency != priceDTO.currency) return false;
        return !(priceStart != null ? !priceStart.equals(priceDTO.priceStart) : priceDTO.priceStart != null);

    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (priceStart != null ? priceStart.hashCode() : 0);
        return result;
    }
}
