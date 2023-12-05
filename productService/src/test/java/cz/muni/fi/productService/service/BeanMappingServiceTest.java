package cz.muni.fi.productService.service;

import cz.muni.fi.productService.dto.CategoryDTO;
import cz.muni.fi.productService.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Set;

@SpringBootTest
public class BeanMappingServiceTest {
    @Autowired
    private BeanMappingService beanMappingService;

	private ProductDTO pRadio;


    @BeforeEach
    public void createOrders(){
		CategoryDTO cElectro = new CategoryDTO();
		cElectro.setId(2L);
		cElectro.setName("Electronics");
		CategoryDTO cSmall = new CategoryDTO();
		cSmall.setId(2L);
    	cSmall.setName("Small");

		pRadio = new ProductDTO();
		pRadio.setId(3L);
		pRadio.setCategories(Set.of(cElectro, cSmall));
		pRadio.setName("Radio");
    }
    
    @Test
    public void shouldMapInnerCategories(){
		ProductDTO dtoRadio = beanMappingService.mapTo(pRadio, ProductDTO.class);
		Assertions.assertEquals(dtoRadio.getId(), pRadio.getId());
		Assertions.assertEquals(dtoRadio.getName(), pRadio.getName());
		Assertions.assertEquals(dtoRadio.getCategories().size(), pRadio.getCategories().size());
    }
}
