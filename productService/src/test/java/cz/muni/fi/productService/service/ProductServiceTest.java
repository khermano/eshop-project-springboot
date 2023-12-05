package cz.muni.fi.productService.service;

import static org.mockito.Mockito.doReturn;
import java.math.BigDecimal;
import cz.muni.fi.productService.dto.NewPriceDTO;
import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Currency;
import cz.muni.fi.productService.exception.EshopServiceException;
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

    @Mock
    private BeanMappingService beanMappingService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    public void setup() throws ServiceException {
        MockitoAnnotations.openMocks(this);
    }
    
    @BeforeEach
    public void prepareTestProduct(){
    	testProduct = new Product();
        Price price = new Price();
        price.setCurrency(Currency.EUR);
        price.setValue(BigDecimal.valueOf(10));
        testProduct.setCurrentPrice(price);
    }
    
    @Test
    public void getPriceValueInCurrency(){
        Price price = new Price();
        price.setCurrency(Currency.CZK);
        price.setValue(BigDecimal.valueOf(27));
        Product p = new Product();
        p.setCurrentPrice(price);
        
        BigDecimal value = productService.getPriceValueInCurrency(p, Currency.CZK);
        Assertions.assertEquals(0, value.compareTo(BigDecimal.valueOf(27)));

        value = productService.getPriceValueInCurrency(p, Currency.EUR);
        Assertions.assertEquals(1, value.compareTo(BigDecimal.valueOf(1)), value.toPlainString());
    }

    @Test
    public void priceChangeByTooMuch(){
        NewPriceDTO newPrice = new NewPriceDTO();
        newPrice.setCurrency(Currency.CZK);
        newPrice.setValue(BigDecimal.valueOf(298));

        Assertions.assertThrows(EshopServiceException.class, () -> productService.changePrice(testProduct, newPrice));
    }
    
    @Test
    public void acceptablePriceChange(){
        NewPriceDTO newPrice = new NewPriceDTO();
        newPrice.setCurrency(Currency.CZK);
        newPrice.setValue(BigDecimal.valueOf(297));

        Price price = new Price();
        price.setCurrency(Currency.CZK);
        price.setValue(BigDecimal.valueOf(297));

        doReturn(price).when(beanMappingService).mapTo(newPrice, Price.class);

        productService.changePrice(testProduct, newPrice);

        Assertions.assertEquals(testProduct.getCurrentPrice().getCurrency(), newPrice.getCurrency());
        Assertions.assertEquals(testProduct.getCurrentPrice().getValue(), newPrice.getValue());
    }
}
