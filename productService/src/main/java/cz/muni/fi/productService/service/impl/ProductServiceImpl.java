package cz.muni.fi.productService.service.impl;

import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Currency;
import cz.muni.fi.productService.exceptions.EshopServiceException;
import cz.muni.fi.productService.repository.PriceRepository;
import cz.muni.fi.productService.repository.ProductRepository;
import cz.muni.fi.productService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private static final Map<AbstractMap.SimpleEntry<Currency, Currency>, BigDecimal> currencyRateCache = new HashMap<>();

	static {
		AbstractMap.SimpleEntry<Currency, Currency> czk_usd = new AbstractMap.SimpleEntry<>(Currency.CZK,
				Currency.USD);
		AbstractMap.SimpleEntry<Currency, Currency> czk_eur = new AbstractMap.SimpleEntry<>(Currency.CZK,
				Currency.EUR);
		AbstractMap.SimpleEntry<Currency, Currency> czk_czk = new AbstractMap.SimpleEntry<>(Currency.CZK,
				Currency.CZK);

		AbstractMap.SimpleEntry<Currency, Currency> usd_czk = new AbstractMap.SimpleEntry<>(Currency.USD,
				Currency.CZK);
		AbstractMap.SimpleEntry<Currency, Currency> usd_eur = new AbstractMap.SimpleEntry<>(Currency.USD,
				Currency.EUR);
		AbstractMap.SimpleEntry<Currency, Currency> usd_usd = new AbstractMap.SimpleEntry<>(Currency.USD,
				Currency.USD);

		AbstractMap.SimpleEntry<Currency, Currency> eur_usd = new AbstractMap.SimpleEntry<>(Currency.EUR,
				Currency.USD);
		AbstractMap.SimpleEntry<Currency, Currency> eur_czk = new AbstractMap.SimpleEntry<>(Currency.EUR,
				Currency.CZK);
		AbstractMap.SimpleEntry<Currency, Currency> eur_eur = new AbstractMap.SimpleEntry<>(Currency.EUR,
				Currency.EUR);

		currencyRateCache.put(czk_czk, BigDecimal.valueOf(1, 0));
		currencyRateCache.put(czk_eur, BigDecimal.valueOf(4, 2));
		currencyRateCache.put(czk_usd, BigDecimal.valueOf(4, 2));

		currencyRateCache.put(usd_czk, BigDecimal.valueOf(27, 0));
		currencyRateCache.put(usd_eur, BigDecimal.valueOf(1, 0));
		currencyRateCache.put(usd_usd, BigDecimal.valueOf(1, 0));

		currencyRateCache.put(eur_czk, BigDecimal.valueOf(27, 0));
		currencyRateCache.put(eur_eur, BigDecimal.valueOf(1, 0));
		currencyRateCache.put(eur_usd, BigDecimal.valueOf(1, 0));
	}

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
		BigDecimal convertRate = getCurrencyRate(
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

	@Override
	public BigDecimal getCurrencyRate(Currency currencyFrom, Currency currencyTo) {
		AbstractMap.SimpleEntry<Currency, Currency> convertCouple = new AbstractMap.SimpleEntry<>(currencyFrom,
				currencyTo);
		if (!currencyRateCache.containsKey(convertCouple)) {
			throw new IllegalArgumentException(
					"There is no existing exchange rate for conversion: "
							+ currencyFrom + "->" + currencyTo);
		}
		return currencyRateCache.get(convertCouple);
	}
}