package cz.muni.fi.productService.service;

import cz.muni.fi.productService.enums.Currency;
import java.math.BigDecimal;

public interface ExchangeService {

	BigDecimal getCurrencyRate(Currency currencyFrom, Currency currencyTo);
}
