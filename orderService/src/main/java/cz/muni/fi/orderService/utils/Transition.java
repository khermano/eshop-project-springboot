package cz.muni.fi.orderService.utils;

import cz.muni.fi.orderService.enums.OrderState;
import lombok.Data;

@Data
public class Transition {
	private OrderState startState;

	private OrderState endState;

	public Transition(OrderState startState, OrderState endState) {
		super();
		this.startState = startState;
		this.endState = endState;
	}
}
