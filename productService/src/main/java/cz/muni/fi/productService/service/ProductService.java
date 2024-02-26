package cz.muni.fi.productService.service;

import java.math.BigDecimal;
import cz.muni.fi.productService.dto.CategoryDTO;
import cz.muni.fi.productService.dto.NewPriceDTO;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Currency;

public interface ProductService {
	Product createProduct(Product p);
	void addCategory(Long productId, Long categoryId);
	void changePrice(Product product, NewPriceDTO newPrice);
	BigDecimal getPriceValueInCurrency(Product p, Currency currency);
	BigDecimal getCurrencyRate(Currency currencyFrom, Currency currencyTo);
}
