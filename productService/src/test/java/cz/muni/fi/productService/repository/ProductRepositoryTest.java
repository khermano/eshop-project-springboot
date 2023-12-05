package cz.muni.fi.productService.repository;

import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Color;
import cz.muni.fi.productService.enums.Currency;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {
	@Autowired
	public ProductRepository productRepository;

	@Autowired
	public PriceRepository priceRepository;

	private Product p1;

	private Product p3;

	@BeforeEach
	public void createProducts() {
		p1 = new Product();
		Product p2 = new Product();
		p3 = new Product();
		Product p4 = new Product();
		Product p5 = new Product();

		Price priceLow = new Price();
		priceLow.setPriceStart(new Date());
		priceLow.setCurrency(Currency.CZK);
		priceLow.setValue(BigDecimal.TEN);
		priceRepository.save(priceLow);

		p1.setName("p1");
		p2.setName("p2");
		p3.setName("product3");
		p4.setName("p4");
		p5.setName("p5");

		p1.addCategoryId(1L);
		p1.setColor(Color.RED);
		p1.setCurrentPrice(priceLow);
		p2.addCategoryId(1L);

		productRepository.save(p1);
		productRepository.save(p2);
		productRepository.save(p3);
		productRepository.save(p4);
		productRepository.save(p5);
	}

	@Test
	public void findAll() {
		List<Product> found = (List<Product>) productRepository.findAll();
		Assertions.assertEquals(found.size(), 5);
	}

	@Test
	public void findCategoryId() {
		Optional<Product> found = productRepository.findById(p1.getId());
		Assertions.assertTrue(found.isPresent());
		Assertions.assertEquals(found.get().getCategoriesId().size(), 1);
		Assertions.assertEquals(found.get().getCategoriesId().iterator().next(), 1L);
	}

	
	@Test
	public void remove() {
		Assertions.assertTrue(productRepository.findById(p3.getId()).isPresent());
		productRepository.delete(p3);
		Assertions.assertFalse(productRepository.findById(p3.getId()).isPresent());
	}

	@Test
	public void find() {
		Optional<Product> found = productRepository.findById(p1.getId());
		Assertions.assertTrue(found.isPresent());
		Assertions.assertEquals(found.get().getName(), "p1");
		Assertions.assertEquals(found.get().getColor(), Color.RED);
		Assertions.assertEquals(found.get().getCurrentPrice().getValue(), BigDecimal.TEN);
	}

	@Test
	public void mimeTypeCannotBeSetWithoutImage() {
		Product p = new Product();
		p.setName("LCD TV");
		p.setImageMimeType("X");
		Assertions.assertThrows(ConstraintViolationException.class, () -> productRepository.save(p));
	}

	@Test
	public void imageCannotBeSetWithoutMimeType() {
		Product p = new Product();
		p.setName("LCD TV");
		p.setImage(new byte[] {});
		Assertions.assertThrows(ConstraintViolationException.class, () -> productRepository.save(p));
	}

	@Test
	public void imageCanBeSetWithMimeType() {
		Product p = new Product();
		p.setName("LCD TV");
		p.setImageMimeType("X");
		p.setImage(new byte[] {});
		productRepository.save(p);
	}
}