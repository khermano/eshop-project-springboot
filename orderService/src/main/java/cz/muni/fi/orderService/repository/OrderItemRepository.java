package cz.muni.fi.orderService.repository;

import cz.fi.muni.pa165.entity.OrderItem;

public interface OrderItemRepository {
	public void create(OrderItem orderItem);
	public OrderItem findById(Long id);
	public void removeById(Long id);
	public void delete(OrderItem orderItem);
}
