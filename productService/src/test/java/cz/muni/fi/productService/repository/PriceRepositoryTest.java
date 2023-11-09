package cz.muni.fi.productService.repository;

import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.enums.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.util.Date;

@DataJpaTest
public class PriceRepositoryTest{
    @Autowired
    public PriceRepository priceRepository;
    private Price savedPrice;

    @BeforeEach
    public void createPrice(){
        Price p = new Price();
        p.setCurrency(Currency.CZK);
        p.setPriceStart(new Date());
        p.setValue(BigDecimal.valueOf(1001));

        savedPrice = priceRepository.save(p);
    }

    @Test
    public void create(){
        Price foundPrice = priceRepository.findById(savedPrice.getId()).orElseThrow(() -> new RuntimeException("Price not found"));
        Assertions.assertEquals(savedPrice.getPriceStart(), foundPrice.getPriceStart());
    }

    @Test
    public void update(){
        savedPrice.setValue(BigDecimal.valueOf(2));
        Price found = priceRepository.findById(savedPrice.getId()).orElseThrow(() -> new RuntimeException("Price not found"));
        Assertions.assertEquals(BigDecimal.valueOf(2), found.getValue());
    }
}
