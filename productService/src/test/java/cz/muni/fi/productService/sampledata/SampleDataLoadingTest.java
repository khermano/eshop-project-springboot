package cz.muni.fi.productService.sampledata;

import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class SampleDataLoadingTest {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoadingTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void createSampleData() {
        log.debug("Starting test");

        List<Product> found = (List<Product>) productRepository.findAll();
        Assertions.assertFalse(found.isEmpty(), "No products");

        Optional<Product> product = productRepository.findById(1L);
        Assertions.assertTrue(product.isPresent());
        List<Price> priceHistory = product.get().getPriceHistory();
        Assertions.assertFalse(priceHistory.isEmpty(), "No prices for product 1");
    }
}
