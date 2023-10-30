package cz.muni.fi.priceService.service.impl;

import cz.fi.muni.pa165.enums.Currency;
import cz.fi.muni.pa165.utils.Tuple;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeServiceImpl implements ExchangeService {
	private static final Map<Tuple<Currency, Currency>, BigDecimal> currencyRateCache = new HashMap<>();

	static {

		Tuple<Currency, Currency> czk_usd = new Tuple<>(Currency.CZK,
				Currency.USD);
		Tuple<Currency, Currency> czk_eur = new Tuple<>(Currency.CZK,
				Currency.EUR);
		Tuple<Currency, Currency> czk_czk = new Tuple<>(Currency.CZK,
				Currency.CZK);

		Tuple<Currency, Currency> usd_czk = new Tuple<>(Currency.USD,
				Currency.CZK);
		Tuple<Currency, Currency> usd_eur = new Tuple<>(Currency.USD,
				Currency.EUR);
		Tuple<Currency, Currency> usd_usd = new Tuple<>(Currency.USD,
				Currency.USD);

		Tuple<Currency, Currency> eur_usd = new Tuple<>(Currency.EUR,
				Currency.USD);
		Tuple<Currency, Currency> eur_czk = new Tuple<>(Currency.EUR,
				Currency.CZK);
		Tuple<Currency, Currency> eur_eur = new Tuple<>(Currency.EUR,
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
		Tuple<Currency, Currency> convertCouple = new Tuple<>(currencyFrom,
				currencyTo);
		if (!currencyRateCache.containsKey(convertCouple)) {
			throw new IllegalArgumentException(
					"There is no existing exchange rate for conversion: "
							+ currencyFrom + "->" + currencyTo);
		}
		return currencyRateCache.get(convertCouple);
	}
}
