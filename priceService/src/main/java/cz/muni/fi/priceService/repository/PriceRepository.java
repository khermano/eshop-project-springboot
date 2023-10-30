package cz.muni.fi.priceService.repository;

import cz.muni.fi.priceService.entity.Price;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends CrudRepository<Price, Long> {
}
