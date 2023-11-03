package cz.muni.fi.priceService.service.impl;

import cz.muni.fi.priceService.entity.Price;
import cz.muni.fi.priceService.enums.Currency;
import cz.muni.fi.priceService.repository.PriceRepository;
import cz.muni.fi.priceService.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class PriceServiceImpl implements PriceService {
	@Autowired
	private PriceRepository priceRepository;
	private static final Map<AbstractMap.SimpleEntry<Currency, Currency>, BigDecimal> currencyRateCache = new HashMap<>();
	private static final Random RANDOM = new Random();

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

	@Override
	public Price createPriceWithoutHistoricalPrices(long value, ZonedDateTime priceStart, Currency currency) {
		Price newPrice = new Price();
		newPrice.setValue(BigDecimal.valueOf(value));
		newPrice.setPriceStart(Date.from(priceStart.toInstant()));
		newPrice.setCurrency(currency);

		priceRepository.save(newPrice);

		return newPrice;
	}

	@Override
	public Price createPriceWithHistoricalPrices(long value, ZonedDateTime priceStart, Currency currency) {
		Price newPrice = new Price();
		newPrice.setValue(BigDecimal.valueOf(value));
		newPrice.setPriceStart(Date.from(priceStart.toInstant()));
		newPrice.setCurrency(currency);

		List<Price> historicalPrices = new ArrayList<>();
		//ZonedDateTime day = ZonedDateTime.from((TemporalAccessor) price.get().getPriceStart());
		ZonedDateTime day = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(7); //toto prerob!!!!!!!!!!!!!!!!!!!!
		for (int i = 0, n = 1 + RANDOM.nextInt(8); i <= n; i++) {
			day = day.minusMonths(1);
			value = value + 1 + RANDOM.nextInt((int) (value / 5l));
			historicalPrices.add(createPriceWithoutHistoricalPrices(value, day, currency));
		}
		newPrice.setHistoricalPrices(historicalPrices);

		priceRepository.save(newPrice);

		return newPrice;
	}
}
