package cz.muni.fi.categoryservice.repository;

import cz.muni.fi.categoryservice.entity.Category;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class CategoryRepositoryTest {
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	public void findAll(){
		Category cat1 = new Category();
		Category cat2 = new Category();
		cat1.setName("cat1");
		cat2.setName("cat2");
		
		categoryRepository.save(cat1);
		categoryRepository.save(cat2);
		
		List<Category> categories  = categoryRepository.findAll();
		Assertions.assertEquals(categories.size(), 2);
		
		Category cat1assert = new Category();
		Category cat2assert = new Category();
		cat1assert.setName("cat1");
		cat2assert.setName("cat2");

		Assertions.assertTrue(categories.contains(cat1assert));
		Assertions.assertTrue(categories.contains(cat2assert));
	}
	
	@Test
	public void nullCategoryNameNotAllowed(){
		Category cat = new Category();
		cat.setName(null);
		Assertions.assertThrows(ConstraintViolationException.class, () -> categoryRepository.save(cat));
	}
	
	@Test
	public void nameIsUnique(){
		Category cat = new Category();
		cat.setName("Electronics");
		categoryRepository.save(cat);
		Category cat2 = new Category();
		cat2.setName("Electronics");
		Assertions.assertThrows(DataAccessException.class, () -> categoryRepository.save(cat2));
	}
	
	@Test()
	public void savesName(){
		Category cat = new Category();
		cat.setName("Electronics");
		categoryRepository.save(cat);
		Assertions.assertNotNull(categoryRepository.findByName("Electronics"));
		Assertions.assertEquals(categoryRepository.findByName("Electronics").getName(), "Electronics");
	}
	
	/**
	 * Checks that null repository object will be empty for not existent ID and also that delete operation works.
	 */
	@Test()
	public void delete(){
		Category cat = new Category();
		cat.setName("Electronics");
		categoryRepository.save(cat);
		Assertions.assertTrue(categoryRepository.findById(cat.getId()).isPresent());
		categoryRepository.delete(cat);
		Assertions.assertFalse(categoryRepository.findById(cat.getId()).isPresent());
	}
	
	/**
	 * Testing that collections on Category is being loaded as expected
	 */
	@Test
	public void productsInCategory(){
		Category categoryElectro = new Category();
		categoryElectro.setName("Electronics");
		categoryRepository.save(categoryElectro);
		Long tvProductId = 1L;
		categoryElectro.addProduct(tvProductId);
		Optional<Category> found = categoryRepository.findById(categoryElectro.getId());
		Assertions.assertTrue(found.isPresent());
		Assertions.assertEquals(found.get().getProductIds().size(), 1);
		Assertions.assertEquals(found.get().getProductIds().iterator().next(), 1L);
	}
}