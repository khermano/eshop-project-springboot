package cz.muni.fi.priceService.sampledata;

import cz.muni.fi.priceService.entity.Price;
import cz.muni.fi.priceService.enums.Currency;
import cz.muni.fi.priceService.repository.PriceRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SampleDataLoading {
    @Autowired
    private PriceRepository priceRepository;

    final static Logger log = LoggerFactory.getLogger(SampleDataLoading.class);

    private Price createPrice(long value, Currency currency) {
        Price price = new Price();
        price.setCurrency(currency);
        //set current price as 7 days ago
        price.setPriceStart(Date.from(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(7).toInstant()));
        price.setValue(BigDecimal.valueOf(value));
        return price;
    }

    @PostConstruct
    public void loadPriceSampleData() {
        List<Price> prices = new ArrayList<>();

        prices.add(createPrice(10000, Currency.CZK));
        prices.add(createPrice(20, Currency.CZK));
        prices.add(createPrice(25, Currency.CZK));
        prices.add(createPrice(15, Currency.CZK));
        prices.add(createPrice(5, Currency.CZK));
        prices.add(createPrice(100, Currency.CZK));
        prices.add(createPrice(200, Currency.CZK));
        prices.add(createPrice(199, Currency.CZK));
        prices.add(createPrice(10, Currency.CZK));
        prices.add(createPrice(50000, Currency.EUR));
        prices.add(createPrice(100, Currency.CZK));
        prices.add(createPrice(15000, Currency.CZK));
        prices.add(createPrice(299, Currency.CZK));
        prices.add(createPrice(60, Currency.CZK));
        prices.add(createPrice(300, Currency.CZK));
        prices.add(createPrice(10000, Currency.CZK));
        prices.add(createPrice(250, Currency.CZK));
        prices.add(createPrice(20000, Currency.CZK));
        prices.add(createPrice(70, Currency.CZK));
        prices.add(createPrice(85, Currency.CZK));
        prices.add(createPrice(60, Currency.CZK));
        prices.add(createPrice(30, Currency.CZK));
        prices.add(createPrice(90, Currency.CZK));
        prices.add(createPrice(99, Currency.CZK));
        prices.add(createPrice(80, Currency.CZK));
        prices.add(createPrice(220, Currency.CZK));

        priceRepository.saveAll(prices);

        log.info("Loaded eShop prices.");
    }
}
