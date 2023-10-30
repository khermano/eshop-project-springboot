package cz.muni.fi.priceService.entity;

import cz.muni.fi.priceService.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Price {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@DecimalMin("0.0")
	@NotNull
	@Column(nullable=false)
	private BigDecimal priceValue;
	
	@NotNull
	private Date priceStart;
	
	@Enumerated
	@NotNull
	private Currency currency;

	public BigDecimal getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(BigDecimal priceValue) {
		this.priceValue = priceValue;
	}


	public Date getPriceStart() {
		return priceStart;
	}

	public void setPriceStart(Date priceStart) {
		this.priceStart = priceStart;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Long  getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());
		result = prime * result
				+ ((priceStart == null) ? 0 : priceStart.hashCode());
		result = prime * result + ((priceValue == null) ? 0 : priceValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Price other = (Price) obj;
		if (currency != other.currency)
			return false;
		if (priceStart == null) {
			if (other.priceStart != null)
				return false;
		} else if (!priceStart.equals(other.priceStart))
			return false;
		if (priceValue == null) {
			if (other.priceValue != null)
				return false;
		} else if (!priceValue.equals(other.priceValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Price{" +
				"id=" + id +
				", value=" + priceValue +
				", priceStart=" + priceStart +
				", currency=" + currency +
				'}';
	}
}
