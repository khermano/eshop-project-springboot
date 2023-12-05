package cz.muni.fi.orderService.repository;

import cz.muni.fi.orderService.entity.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
}
