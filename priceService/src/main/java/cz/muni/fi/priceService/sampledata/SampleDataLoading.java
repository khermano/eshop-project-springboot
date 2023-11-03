package cz.muni.fi.priceService.sampledata;

import cz.muni.fi.priceService.entity.Price;
import cz.muni.fi.priceService.enums.Currency;
import cz.muni.fi.priceService.repository.PriceRepository;
import cz.muni.fi.priceService.service.PriceService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class SampleDataLoading {
    @Autowired
    private PriceService priceService;
    @Autowired
    private PriceRepository priceRepository;

    final static Logger log = LoggerFactory.getLogger(SampleDataLoading.class);

    @PostConstruct
    public void loadPriceSampleData() {
        //set current price as 7 days ago
        ZonedDateTime day = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(7);

        Price amberPriceId = priceService.createPriceWithHistoricalPrices(10000, day, Currency.CZK);
        Price blackberriesPriceId = priceService.createPriceWithHistoricalPrices(20, day, Currency.CZK);
        Price blueberriesPriceId = priceService.createPriceWithHistoricalPrices(25, day, Currency.CZK);
        Price chilliPriceId = priceService.createPriceWithHistoricalPrices(15, day, Currency.CZK);
        Price clampsPriceId = priceService.createPriceWithHistoricalPrices(5, day, Currency.CZK);
        Price coffeePriceId = priceService.createPriceWithHistoricalPrices(100, day, Currency.CZK);
        Price mousePriceId = priceService.createPriceWithHistoricalPrices(200, day, Currency.CZK);
        Price cowPriceId = priceService.createPriceWithHistoricalPrices(199, day, Currency.CZK);
        Price crayonsPriceId = priceService.createPriceWithHistoricalPrices(10, day, Currency.CZK);
        Price diamondsPriceId = priceService.createPriceWithHistoricalPrices(50000, day, Currency.EUR);
        Price figsPriceId = priceService.createPriceWithHistoricalPrices(100, day, Currency.CZK);
        Price goldPriceId = priceService.createPriceWithHistoricalPrices(15000, day, Currency.CZK);
        Price horsePriceId = priceService.createPriceWithHistoricalPrices(299, day, Currency.CZK);
        Price limesPriceId = priceService.createPriceWithHistoricalPrices(60, day, Currency.CZK);
        Price mixedFlowersPriceId = priceService.createPriceWithHistoricalPrices(300, day, Currency.CZK);
        Price monitorPriceId = priceService.createPriceWithHistoricalPrices(10000, day, Currency.CZK);
        Price narcissusPriceId = priceService.createPriceWithHistoricalPrices(250, day, Currency.CZK);
        Price notebookPriceId = priceService.createPriceWithHistoricalPrices(20000, day, Currency.CZK);
        Price orangesPriceId = priceService.createPriceWithHistoricalPrices(70, day, Currency.CZK);
        Price pearsPriceId = priceService.createPriceWithHistoricalPrices(85, day, Currency.CZK);
        Price peppersPriceId = priceService.createPriceWithHistoricalPrices(60, day, Currency.CZK);
        Price pinsPriceId = priceService.createPriceWithHistoricalPrices(30, day, Currency.CZK);
        Price raspberriesPriceId = priceService.createPriceWithHistoricalPrices(90, day, Currency.CZK);
        Price duckPriceId = priceService.createPriceWithHistoricalPrices(99, day, Currency.CZK);
        Price strawberriesPriceId = priceService.createPriceWithHistoricalPrices(80, day, Currency.CZK);
        Price tulipPriceId = priceService.createPriceWithHistoricalPrices(220, day, Currency.CZK);

        log.info("Loaded eShop prices.");

        // fetch all users
        // THIS IS ONLY THE CONTROL STATEMENT AND IT'S MEANT TO BE REMOVED LATER
        System.out.println("Users found with findAll():");
        System.out.println("---------------------------");
        for (Price price : priceRepository.findAll()) {
            System.out.println(price.toString());
        }
        System.out.println();
    }
}
