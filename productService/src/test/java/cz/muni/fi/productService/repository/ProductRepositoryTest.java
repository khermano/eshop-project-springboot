package cz.muni.fi.productService.repository;

import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Color;
import cz.muni.fi.productService.enums.Currency;
import org.hibernate.exception.ConstraintViolationException;
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
//	@PersistenceContext
//	public EntityManager em;

	@Autowired
	public ProductRepository productRepository;

	@Autowired
	public PriceRepository priceRepository;

//	@Autowired
//	public CategoryDao categoryDao;

	private Product p1;
	private Product p2;
	private Product p3;
	private Product p4;
	private Product p5;

	@BeforeEach
	public void createProducts() {
		p1 = new Product();
		p2 = new Product();
		p3 = new Product();
		p4 = new Product();
		p5 = new Product();

		Price priceLow = new Price();
		priceLow.setPriceStart(new Date());
		priceLow.setCurrency(Currency.CZK);
		priceLow.setValue(BigDecimal.TEN);
		priceRepository.save(priceLow);

//		Category cat = new Category();
//		cat.setName("cat");
//		categoryDao.create(cat);

		p1.setName("p1");
		p2.setName("p2");
		p3.setName("product3");
		p4.setName("p4");
		p5.setName("p5");

		p1.addCategoryId(1l);
		p1.setColor(Color.RED);
		p1.setCurrentPrice(priceLow);
		p2.addCategoryId(1l);

		productRepository.save(p1);
		productRepository.save(p2);
		productRepository.save(p3);
		productRepository.save(p4);
		productRepository.save(p5);
	}

	@Test
	public void findAll() {
		List<Product> found = productRepository.findAll();
		Assertions.assertEquals(found.size(), 5);
	}


	@Test
	public void findCategoryId() {
		Optional<Product> found = productRepository.findById(p1.getId());
		if (found.isPresent()) {
			Assertions.assertEquals(found.get().getCategoriesId().size(), 1);
			Assertions.assertEquals(found.get().getCategoriesId().iterator().next(), 1l);
		} else {
			throw new IllegalArgumentException("Product is null");
		}
	}

	
	@Test
	public void remove() {
		Assertions.assertTrue(productRepository.findById(p3.getId()).isPresent());
		productRepository.delete(p3);
		Assertions.assertFalse(productRepository.findById(p3.getId()).isPresent());
	}

	@Test
	public void findByName() {
		Assertions.assertEquals(productRepository.findByName("p").size(), 5);
		Assertions.assertEquals(productRepository.findByName("asdf").size(), 0);
		Assertions.assertEquals(productRepository.findByName("product3").size(), 1);
	}

	@Test
	public void find() {
		Optional<Product> found = productRepository.findById(p1.getId());
		if (found.isPresent()) {
			Assertions.assertEquals(found.get().getName(), "p1");
			Assertions.assertEquals(found.get().getColor(), Color.RED);
			Assertions.assertEquals(found.get().getCurrentPrice().getValue(), BigDecimal.TEN);
		} else {
			throw new IllegalArgumentException("Product is null");
		}
	}

	@Test
	public void mimeTypeCannotBeSetWithoutImage() {
		Product p = new Product();
		p.setName("LCD TV");
		p.setImageMimeType("X");
		productRepository.save(p);
		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			productRepository.save(p);
		});
	}

	@Test
	public void imageCannotBeSetWithoutMimeType() {
		Product p = new Product();
		p.setName("LCD TV");
		p.setImage(new byte[] {});
		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			productRepository.save(p);
		});
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