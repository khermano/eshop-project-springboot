package cz.muni.fi.orderService.repository;

import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.enums.OrderState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class OrderRepositoryTest {
	@Autowired
	public OrderRepository orderRepository;

	private Order o1;

	private Date date1;

	private final Long user1Id = 1L;

	@BeforeEach
	public void createOrders(){
		Calendar cal = Calendar.getInstance();
		cal.set(2015, Calendar.JANUARY, 1);
		date1 = cal.getTime();
		cal.set(2015, Calendar.APRIL, 5);
		Date date2 = cal.getTime();
		cal.set(2015, Calendar.JUNE, 6);
		Date date3 = cal.getTime();
		
		o1 = new Order();
		o1.setCreated(date1);
		o1.setState(OrderState.CANCELED);
		o1.setUserId(user1Id);

		Order o2 = new Order();
		o2.setCreated(date2);
		o2.setState(OrderState.RECEIVED);
		o2.setUserId(user1Id);

		Order o3 = new Order();
		o3.setCreated(date3);
		o3.setState(OrderState.CANCELED);
		Long user2Id = 2L;
		o3.setUserId(user2Id);
		
		orderRepository.save(o1);
		orderRepository.save(o2);
		orderRepository.save(o3);
	}
	
	@Test
	public void nonExistentReturnsNull() {
		Optional<Order> found = orderRepository.findById(321321L);
		Assertions.assertFalse(found.isPresent());
	}

	
	@Test
	public void find() {
		Optional<Order> found = orderRepository.findById(o1.getId());

		Assertions.assertTrue(found.isPresent());
		Assertions.assertEquals(found.get().getCreated(), date1);
		Assertions.assertEquals(found.get().getState(), OrderState.CANCELED);
		Assertions.assertEquals(found.get().getUserId(), user1Id);
	}

	@Test
	public void findByUser() {
		List<Order> orders = orderRepository.findByUserId(user1Id);
		Assertions.assertEquals(orders.size(), 2);
	}

	@Test
	public void getOrdersWithState() {
		Assertions.assertEquals(orderRepository.findByState(OrderState.SHIPPED).size(),0);
		
		List<Order> canceled = orderRepository.findByState(OrderState.CANCELED);
		List<Order> received = orderRepository.findByState(OrderState.RECEIVED);

		Assertions.assertEquals(canceled.size(), 2);
		Assertions.assertEquals(received.size(), 1);
	}

	@Test
	public void getOrdersCreatedBetween() {
		Calendar cal = Calendar.getInstance();
		cal.set(2016, Calendar.JANUARY, 1);
		Date date1 = cal.getTime();
		cal.set(2017, Calendar.APRIL, 5);
		Date date2 = cal.getTime();

		Assertions.assertEquals(orderRepository.getOrdersCreatedBetween(date1, date2, OrderState.RECEIVED).size(),0);
		
		cal.set(2015, Calendar.JANUARY, 1,0,0,0);
		Date date3 = cal.getTime();
		cal.set(2015, Calendar.MAY, 5,0,0,0);
		Date date4 = cal.getTime();
		Assertions.assertEquals(orderRepository.getOrdersCreatedBetween(date3, date4, OrderState.CANCELED).size(),1);
	}
}
