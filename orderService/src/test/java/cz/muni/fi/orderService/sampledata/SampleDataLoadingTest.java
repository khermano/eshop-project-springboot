package cz.muni.fi.orderService.sampledata;

import cz.muni.fi.orderService.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests data loading.
 */
@SpringBootTest
public class SampleDataLoadingTest {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoadingTest.class);

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void createSampleData() {
        log.debug("Starting test");

        Assertions.assertFalse(orderRepository.findAll().isEmpty(), "No orders");
    }
}
