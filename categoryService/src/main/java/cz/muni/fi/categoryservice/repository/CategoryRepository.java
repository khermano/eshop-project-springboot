package cz.muni.fi.categoryservice.repository;

import cz.muni.fi.categoryservice.entity.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
