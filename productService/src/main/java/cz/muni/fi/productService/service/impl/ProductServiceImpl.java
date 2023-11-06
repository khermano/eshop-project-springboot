package cz.muni.fi.productService.service.impl;

import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Currency;
import cz.muni.fi.productService.exceptions.EshopServiceException;
import cz.muni.fi.productService.repository.PriceRepository;
import cz.muni.fi.productService.repository.ProductRepository;
import cz.muni.fi.productService.service.ExchangeService;
import cz.muni.fi.productService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the {@link ProductService}. This class is part of the
 * service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 */
@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PriceRepository priceRepository;
	
	@Autowired
	private ExchangeService exchangeService;

	@Override
	public Product createProduct(Product product) {
        List<Price> priceHistory = product.getPriceHistory();
        for (Price price : priceHistory) {
			priceRepository.save(price);
		}
		productRepository.save(product);
		return product;
	}

	@Override
	public BigDecimal getPriceValueInCurrency(Product p, Currency currency) {
		BigDecimal convertRate = exchangeService.getCurrencyRate(
				p.getCurrentPrice().getCurrency(), currency);

        return p.getCurrentPrice().getValue().multiply(convertRate)
				.setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public void changePrice(Product p, Price newPrice) {
		BigDecimal oldPriceInNewCurrency = getPriceValueInCurrency(p, newPrice.getCurrency());

		BigDecimal difference = oldPriceInNewCurrency
				.subtract(newPrice.getValue());
		BigDecimal percents = difference.abs().divide(
				oldPriceInNewCurrency, 5, RoundingMode.HALF_UP);
		if (percents.compareTo(new BigDecimal("0.1")) > 0) {
			throw new EshopServiceException(
					"It is not allowed to change the price by more than 10%");
		}
		newPrice.setPriceStart(new Date());
		priceRepository.save(newPrice);
		p.addHistoricalPrice(p.getCurrentPrice());
		p.setCurrentPrice(newPrice);
	}

	@Override
	public void addCategory(Product product, Long categoryId) {
		if (product.getCategoriesId().contains(categoryId)) {
			throw new EshopServiceException(
					"Product already contains this category. Product: "
							+ product.getId() + ", categoryId: "
							+ categoryId);
		}
		product.addCategoryId(categoryId);
		//TODO we need to make sure that category is created in the categoryService, not only we ID stores in this service
	}

	@Override
	public void removeCategory(Product product, Long categoryId) {
		product.removeCategoryId(categoryId);
		//TODO same as up
	}
}
