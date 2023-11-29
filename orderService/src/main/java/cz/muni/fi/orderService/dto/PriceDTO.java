package cz.muni.fi.orderService.dto;

import cz.muni.fi.orderService.enums.Currency;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PriceDTO {
	private Long id;

	private BigDecimal value;

	private Currency currency;

	private Date priceStart;
}
