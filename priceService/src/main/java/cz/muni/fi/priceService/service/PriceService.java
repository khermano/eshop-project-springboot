package cz.muni.fi.priceService.service;

import cz.muni.fi.priceService.entity.Price;
import cz.muni.fi.priceService.enums.Currency;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface PriceService {

	BigDecimal getCurrencyRate(Currency currencyFrom, Currency currencyTo);

	/**
	 * Creates new price (generated as historical prices) and stores it in the DB
	 * @param value - amount of the price
	 * @param priceStart - ZonedDateTime when the price started
	 * @param currency of the price
	 * @return price which is stored in the DB
	 */
	Price createPriceWithoutHistoricalPrices(long value, ZonedDateTime priceStart, Currency currency);

	/**
	 * Creates new price with generated historical prices and stores it in the DB
	 * @param value - amount of the price
	 * @param priceStart - ZonedDateTime when the price started
	 * @param currency of the price
	 * @return price which is stored in the DB
	 */
	Price createPriceWithHistoricalPrices(long value, ZonedDateTime priceStart, Currency currency);
}
