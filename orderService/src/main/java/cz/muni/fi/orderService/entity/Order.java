package cz.muni.fi.orderService.entity;

import cz.muni.fi.orderService.enums.OrderState;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="PRODUCT_ORDER")
@Data
public class Order {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private Long userId;
	
	@OneToMany
	@NotNull
	@Getter(AccessLevel.NONE)
	private List<OrderItem> orderItems = new ArrayList<>();
		
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Enumerated
	@NotNull
	private OrderState state;

	public List<OrderItem> getOrderItems() {
		return Collections.unmodifiableList(orderItems);
	}

	public void addOrderItem(OrderItem p) {
		orderItems.add(p);
	}
}
