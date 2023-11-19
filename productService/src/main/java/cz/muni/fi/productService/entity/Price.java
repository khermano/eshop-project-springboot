package cz.muni.fi.productService.entity;

import cz.muni.fi.productService.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Price {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@DecimalMin("0.0")
	@NotNull
	@Column(nullable=false)
	private BigDecimal value;
	
	@NotNull
	private Date priceStart;
	
	@Enumerated
	@NotNull
	private Currency currency;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());
		result = prime * result
				+ ((priceStart == null) ? 0 : priceStart.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Price{" +
				"id=" + id +
				", value=" + value +
				", priceStart=" + priceStart +
				", currency=" + currency +
				'}';
	}
}
