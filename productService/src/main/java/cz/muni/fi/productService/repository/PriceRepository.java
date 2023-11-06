package cz.muni.fi.productService.repository;

import cz.muni.fi.productService.entity.Price;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends CrudRepository<Price, Long> {
}
