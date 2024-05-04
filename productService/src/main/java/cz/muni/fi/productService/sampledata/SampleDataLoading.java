package cz.muni.fi.productService.sampledata;

import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Color;
import cz.muni.fi.productService.enums.Currency;
import cz.muni.fi.productService.service.ProductService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

@Component
public class SampleDataLoading {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoading.class);
    private static final Random RANDOM = new Random();
    public static final String JPEG = "image/jpeg";

    @Autowired
    private ProductService productService;

    private Price createPrice(long priceValue, ZonedDateTime priceStart, Currency currency) {
        Price price = new Price();
        price.setCurrency(currency);
        price.setPriceStart(Date.from(priceStart.toInstant()));
        price.setValue(BigDecimal.valueOf(priceValue));
        return price;
    }
    private void createProduct(String name, String description, String imageFile, long priceValue, Currency currency, Color color, Long... categoryIds) throws IOException {
        Product product = new Product();
        for (Long categoryId : categoryIds) {
            product.addCategoryId(categoryId);
        }
        product.setColor(color);
        product.setName(name);
        product.setDescription(description);

        //set current price as 7 days ago
        ZonedDateTime day = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(7);
        Price price = createPrice(priceValue, day, currency);
        product.setCurrentPrice(price);
        product.addHistoricalPrice(price);

        //generate randomly higher historical prices
        for (int i = 0; i <= 5; i++) {
            day = day.minusMonths(1);
            priceValue = priceValue + 1 + RANDOM.nextInt((int) (priceValue / 5L));
            product.addHistoricalPrice(createPrice(priceValue, day, currency));
        }
        product.setAddedDate(Date.from(day.toInstant()));

        product.setImage(readImage("images/" + imageFile));
        product.setImageMimeType(JPEG);
        productService.createProduct(product);
    }

    private byte[] readImage(String file) throws IOException {
        try (InputStream is = this.getClass().getResourceAsStream("/" + file)) {
            int nRead;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            while (true) {
                assert is != null;
                if ((nRead = is.read(data, 0, data.length)) == -1) break;
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        }
    }

    @PostConstruct
    public void loadProductSampleData() throws IOException {
        Long foodId = 1L;
        Long officeId = 2L;
        Long flowersId = 3L;
        Long toysId = 4L;
        Long presentsId = 5L;

        createProduct("Amber", "", "amber.jpg", 10000, Currency.CZK, Color.UNDEFINED, presentsId);
        createProduct("Blackberries", "", "blackberries.jpg", 20, Currency.CZK, Color.BLACK, foodId);
        createProduct("Blueberries", "", "blueberries.jpg", 25, Currency.CZK, Color.BLUE, foodId);
        createProduct("Chilli", "", "chilli.jpg", 15, Currency.CZK, Color.RED, foodId);
        createProduct("Clamps", "", "clamps.jpg", 5, Currency.CZK, Color.UNDEFINED, officeId);
        createProduct("Coffee", "", "coffee.jpg", 100, Currency.CZK, Color.BROWN, foodId, officeId, presentsId);
        createProduct("Mouse", "", "computer-mouse.jpg", 200, Currency.CZK, Color.BLACK, officeId);
        createProduct("Cow", "", "cow.jpg", 199, Currency.CZK, Color.BROWN, toysId);
        createProduct("Crayons", "", "crayons.jpg", 10, Currency.CZK, Color.BLACK, officeId);
        createProduct("Diamond", "Diamonds are forever.", "diamond.jpg", 50000, Currency.EUR, Color.WHITE, presentsId);
        createProduct("Figs", "", "figs.jpg", 100, Currency.CZK, Color.BROWN, foodId);
        createProduct("Gold", "", "gold.jpg", 15000, Currency.CZK, Color.YELLOW, presentsId);
        createProduct("Horse", "", "horse.jpg", 299, Currency.CZK, Color.BROWN, toysId);
        createProduct("Limes", "", "limes.jpg", 60, Currency.CZK, Color.GREEN, foodId);
        createProduct("Mixed flowers", "", "mixed-flowers.jpg", 300, Currency.CZK, Color.UNDEFINED, flowersId);
        createProduct("Monitor", "", "monitor.jpg", 10000, Currency.CZK, Color.BLACK, officeId);
        createProduct("Narcissus", "", "narcissus.jpg", 250, Currency.CZK, Color.YELLOW, flowersId);
        createProduct("Notebook", "", "notebook.jpg", 20000, Currency.CZK, Color.BLACK, officeId);
        createProduct("Oranges", "", "oranges.jpg", 70, Currency.CZK, Color.ORANGE, foodId);
        createProduct("Pears", "", "pears.jpg", 85, Currency.CZK, Color.GREEN, foodId);
        createProduct("Peppers", "", "peppers.jpg", 60, Currency.CZK, Color.UNDEFINED, foodId);
        createProduct("Pins", "", "pins.jpg", 30, Currency.CZK, Color.UNDEFINED, officeId);
        createProduct("Raspberries", "", "raspberries.jpg", 90, Currency.CZK, Color.PINK, foodId);
        createProduct("Rubber ducks", "", "rubber-duckies.jpg", 99, Currency.CZK, Color.YELLOW, toysId);
        createProduct("Strawberries", "Very tasty and exceptionally red strawberries.", "strawberries.jpg", 80, Currency.CZK, Color.RED, foodId);
        createProduct("Tulip", "", "tulip.jpg", 220, Currency.CZK, Color.RED, flowersId);

        this.createTestProducts();

        log.info("Loaded eShop products.");
    }

    private void createTestProducts() throws IOException {
        Currency[] currencies = Currency.values();
        Color[] colors = Color.values();
        for (int i = 1; i <= 1000; i++) {
            createProduct("Product" + i, "", "blueberries.jpg", 10000, currencies[i % 3], colors[i % 12], Long.valueOf(i));
        }
    }
}
