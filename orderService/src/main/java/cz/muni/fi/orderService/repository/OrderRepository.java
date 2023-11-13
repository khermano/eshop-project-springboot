package cz.muni.fi.orderService.repository;

import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);

	List<Order> findByState(OrderState state);

	@Query(value = "SELECT o FROM Order o WHERE o.state = :state AND  o.created BETWEEN :startDate AND :endDate")
	List<Order> getOrdersCreatedBetween(Date startDate, Date endDate, OrderState state);
}
