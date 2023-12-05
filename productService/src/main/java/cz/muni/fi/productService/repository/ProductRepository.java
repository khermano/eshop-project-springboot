package cz.muni.fi.productService.repository;

import cz.muni.fi.productService.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
