package cz.muni.fi.orderService.utils;

import cz.muni.fi.orderService.enums.OrderState;

public class Transition {
	private OrderState startState;

	private OrderState endState;

	public Transition(OrderState startState, OrderState endState) {
		this.startState = startState;
		this.endState = endState;
	}

	public OrderState getStartState() {
		return startState;
	}

	public void setStartState(OrderState startState) {
		this.startState = startState;
	}

	public OrderState getEndState() {
		return endState;
	}

	public void setEndState(OrderState endState) {
		this.endState = endState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endState == null) ? 0 : endState.hashCode());
		return prime * result
				+ ((startState == null) ? 0 : startState.hashCode());
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Transition other = (Transition) obj;
		if (endState != other.endState) {
			return false;
		}
        return startState == other.startState;
    }
}
