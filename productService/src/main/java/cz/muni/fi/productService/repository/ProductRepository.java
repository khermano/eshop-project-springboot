package cz.muni.fi.productService.repository;

import cz.muni.fi.productService.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query(value = "SELECT p FROM Product p WHERE p.name LIKE %:name%")
	List<Product> findByName(String name);
}
