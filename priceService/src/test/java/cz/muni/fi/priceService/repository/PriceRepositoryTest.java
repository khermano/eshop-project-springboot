package cz.muni.fi.priceService.repository;

import cz.muni.fi.priceService.entity.Price;
import cz.muni.fi.priceService.enums.Currency;
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
        p.setPriceValue(new BigDecimal("1001"));

        savedPrice = priceRepository.save(p);
    }

    @Test
    public void create(){
        Price foundPrice = priceRepository.findById(savedPrice.getId()).orElseThrow(() -> new RuntimeException("price not found"));
        Assertions.assertEquals(savedPrice.getPriceStart(), foundPrice.getPriceStart());
    }

    @Test
    public void update(){
        savedPrice.setPriceValue(new BigDecimal("2"));
        Price found = priceRepository.findById(savedPrice.getId()).orElseThrow(() -> new RuntimeException("price not found"));
        Assertions.assertEquals(new BigDecimal("2"), found.getPriceValue());
    }
}
