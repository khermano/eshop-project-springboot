package cz.muni.fi.priceService.service.impl;

import cz.muni.fi.priceService.enums.Currency;
import cz.muni.fi.priceService.service.ExchangeService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeServiceImpl implements ExchangeService {
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
