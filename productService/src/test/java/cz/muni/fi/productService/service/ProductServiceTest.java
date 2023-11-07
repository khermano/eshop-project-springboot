package cz.muni.fi.productService.service;

import static org.mockito.Mockito.verify;
import java.math.BigDecimal;
import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Currency;
import cz.muni.fi.productService.exceptions.EshopServiceException;
import cz.muni.fi.productService.repository.PriceRepository;
import cz.muni.fi.productService.service.impl.ProductServiceImpl;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setup() throws ServiceException
    {
        MockitoAnnotations.openMocks(this);
    }
    
    private Product testProduct;
    
    @BeforeEach
    public void prepareTestProduct(){
    	testProduct = new Product();
        Price price = new Price();
        price.setCurrency(Currency.EUR);
        price.setValue(new BigDecimal(10));
        testProduct.setCurrentPrice(price);
    }
    
    @Test
    public void getPriceValueInCurrency(){
    	Product p = new Product();
        Price price = new Price();
        price.setCurrency(Currency.CZK);
        price.setValue(new BigDecimal(27));
        p.setCurrentPrice(price);
        
        BigDecimal value = productService.getPriceValueInCurrency(p, Currency.CZK);
        Assertions.assertEquals(0, value.compareTo(BigDecimal.valueOf(27, 0)));

        value = productService.getPriceValueInCurrency(p, Currency.EUR);
        Assertions.assertEquals(1, value.compareTo(BigDecimal.valueOf(1, 0)), value.toPlainString());
    }

    @Test
    public void priceChangeByTooMuch(){
        Price newPrice = new Price();
        newPrice.setCurrency(Currency.CZK);
        newPrice.setValue(BigDecimal.valueOf(298));
        Assertions.assertThrows(EshopServiceException.class, () -> {
            productService.changePrice(testProduct, newPrice);
        });
    }
    
    @Test
    public void acceptablePriceChange(){
        Price newPrice = new Price();
        newPrice.setCurrency(Currency.CZK);
        newPrice.setValue(BigDecimal.valueOf(297));        
        productService.changePrice(testProduct, newPrice);
        
        verify(priceRepository).save(newPrice);
        Assertions.assertEquals(testProduct.getCurrentPrice(), newPrice);
        Assertions.assertEquals(testProduct.getCurrentPrice().getPriceStart(), newPrice.getPriceStart());
    }
}
