package cz.muni.fi.categoryservice.repository;

import cz.muni.fi.categoryservice.entity.Category;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import java.util.List;

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

		List<Category> categories  = (List<Category>) categoryRepository.findAll();
		Assertions.assertEquals(categories.size(), 2);

		Assertions.assertEquals(categories.get(0).getName(), cat1.getName());
		Assertions.assertEquals(categories.get(1).getName(), cat2.getName());
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
	public void delete(){
		Category cat = new Category();
		cat.setName("Electronics");
		categoryRepository.save(cat);

		Assertions.assertTrue(categoryRepository.findById(cat.getId()).isPresent());
		categoryRepository.delete(cat);
		Assertions.assertFalse(categoryRepository.findById(cat.getId()).isPresent());
	}
}
