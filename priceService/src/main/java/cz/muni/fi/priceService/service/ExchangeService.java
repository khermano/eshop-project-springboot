package cz.muni.fi.priceService.service;

import cz.muni.fi.priceService.enums.Currency;
import java.math.BigDecimal;

public interface ExchangeService {

	BigDecimal getCurrencyRate(Currency currencyFrom, Currency currencyTo);
}
