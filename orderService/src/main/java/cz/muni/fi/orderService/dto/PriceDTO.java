package cz.muni.fi.orderService.dto;

import cz.muni.fi.orderService.enums.Currency;
import java.math.BigDecimal;
import java.util.Date;

public class PriceDTO {
	private Long id;

	private BigDecimal value;

	private Currency currency;

	private Date priceStart;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Date getPriceStart() {
		return priceStart;
	}

	public void setPriceStart(Date priceStart) {
		this.priceStart = priceStart;
	}
}
