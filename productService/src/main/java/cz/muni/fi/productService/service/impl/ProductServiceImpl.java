package cz.muni.fi.productService.service.impl;

import cz.muni.fi.productService.dto.CategoryDTO;
import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Currency;
import cz.muni.fi.productService.exception.EshopServiceException;
import cz.muni.fi.productService.exception.InvalidParameterException;
import cz.muni.fi.productService.repository.PriceRepository;
import cz.muni.fi.productService.repository.ProductRepository;
import cz.muni.fi.productService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

		currencyRateCache.put(czk_czk, BigDecimal.valueOf(1));
		currencyRateCache.put(czk_eur, BigDecimal.valueOf(0.04));
		currencyRateCache.put(czk_usd, BigDecimal.valueOf(0.04));

		currencyRateCache.put(usd_czk, BigDecimal.valueOf(27));
		currencyRateCache.put(usd_eur, BigDecimal.valueOf(1));
		currencyRateCache.put(usd_usd, BigDecimal.valueOf(1));

		currencyRateCache.put(eur_czk, BigDecimal.valueOf(27));
		currencyRateCache.put(eur_eur, BigDecimal.valueOf(1));
		currencyRateCache.put(eur_usd, BigDecimal.valueOf(1));
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
		if (percents.compareTo(BigDecimal.valueOf(0.1)) > 0) {
			throw new EshopServiceException(
					"It is not allowed to change the price by more than 10%");
		}
		newPrice.setPriceStart(new Date());
		priceRepository.save(newPrice);
		p.addHistoricalPrice(p.getCurrentPrice());
		p.setCurrentPrice(newPrice);
	}

	@Override
	public void addCategory(Long productId, CategoryDTO category) throws IOException {
		Long categoryId = category.getId();

		URL url = new URL("http://localhost:8082/eshop-rest/categories/" + categoryId.intValue());
		HttpURLConnection con = createConnectionForGet(url);

		Optional<Product> product = productRepository.findById(productId);

		if (product.isPresent() && (product.get().getCategoriesId().contains(categoryId) || con.getResponseCode() == 200)) {
			throw new EshopServiceException(
					"Product already contains this category. Product: "
							+ product.get().getId() + ", categoryId: "
							+ categoryId);
		}
		else if (product.isPresent() && !product.get().getCategoriesId().contains(categoryId) && con.getResponseCode() == 404) {
			url = new URL("http://localhost:8082/eshop-rest/categories/create");
			con = createConnectionForPost(url);
			String jsonInputString = "{\"id\":\"" + categoryId + "\", \"name\":\"" + category.getName() + "\"}";
			try(OutputStream os = con.getOutputStream()) {
				byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			if (con.getResponseCode() != 200) {
				throw new InvalidParameterException();
			}

			product.get().addCategoryId(categoryId);
			productRepository.save(product.get());
		}
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

	private HttpURLConnection createConnectionForGet(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		return con;
	}

	private HttpURLConnection createConnectionForPost(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		return con;
	}
}
