package cz.muni.fi.productService.service;

import java.math.BigDecimal;
import java.util.List;
import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Currency;

public interface ProductService {
	Product createProduct(Product p);
	void addCategory(Product product, Long categoryId);
	void removeCategory(Product product, Long categoryId);
	void changePrice(Product product, Price newPrice);
	BigDecimal getPriceValueInCurrency(Product p, Currency currency);
	
}
