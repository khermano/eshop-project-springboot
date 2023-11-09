package cz.muni.fi.productService.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.productService.dto.ProductCreateDTO;
import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Color;
import cz.muni.fi.productService.enums.Currency;
import cz.muni.fi.productService.repository.ProductRepository;
import cz.muni.fi.productService.service.BeanMappingService;
import cz.muni.fi.productService.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
	@Mock
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private BeanMappingService beanMappingService;

	@InjectMocks
	private ProductController productController;

	private MockMvc mockMvc;
        

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = standaloneSetup(productController).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

	@Test
	public void debugTest() throws Exception {
		doReturn(Collections.unmodifiableList(this.createProducts())).when(
				productRepository).findAll();
		mockMvc.perform(get("/product"));
	}

	@Test
	public void getAllProducts() throws Exception {
		doReturn(Collections.unmodifiableList(this.createProducts())).when(
				productRepository).findAll();

		mockMvc.perform(get("/product"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(
						jsonPath("$.[?(@.id==10)].name").value("Raspberry PI"))
				.andExpect(jsonPath("$.[?(@.id==20)].name").value("Arduino"));
	}

	@Test
	public void getValidProduct() throws Exception {
		List<Optional<Product>> products = this.createProducts();

		doReturn(products.get(0)).when(productRepository).findById(10l);
		doReturn(products.get(1)).when(productRepository).findById(20l);

		mockMvc.perform(get("/product/10"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.name").value("Raspberry PI"));
		mockMvc.perform(get("/product/20"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.name").value("Arduino"));
	}

	@Test
	public void getInvalidProduct() throws Exception {
		doReturn(Optional.empty()).when(productRepository).findById(1l);

		mockMvc.perform(get("/product/1")).andExpect(
				status().is4xxClientError());
	}

	@Test
	public void deleteProduct() throws Exception {
		mockMvc.perform(delete("/product/10"))
				.andExpect(status().isOk());
	}
        
	@Test
	public void deleteProductNonExisting() throws Exception {
		doThrow(new RuntimeException("the product does not exist")).when(productRepository).deleteById(20l);

		mockMvc.perform(delete("/product/20"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void createProduct() throws Exception {
		ProductCreateDTO productCreateDTO = new ProductCreateDTO();

		Product mockedProduct = new Product();
		mockedProduct.setId(1L);

		doReturn(mockedProduct).when(beanMappingService).mapTo(productCreateDTO, Product.class);
		doReturn(mockedProduct).when(productService).createProduct(any(Product.class));
		doReturn(Optional.of(mockedProduct)).when(productRepository).findById(1L);

		String json = this.convertObjectToJsonBytes(productCreateDTO);

		System.out.println(json);

		mockMvc.perform(
				post("/product/create").contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void updateProduct() throws Exception {
		List<Optional<Product>> products = this.createProducts();

		doReturn(products.get(0)).when(productRepository).findById(10l);

		doNothing().when(productService).changePrice(any(Product.class), any(Price.class));
		Price newPrice = new Price();
		
		String json = this.convertObjectToJsonBytes(newPrice);

		mockMvc.perform(
				put("/product/10").contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void addCategory() throws Exception {
		List<Optional<Product>> products = this.createProducts();

		doReturn(products.get(0)).when(productRepository).findById(10l);

		Long categoryId = 1l;

		String json = this.convertObjectToJsonBytes(categoryId);

		mockMvc.perform(
				post("/product/10/categories").contentType(
						MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

	private List<Optional<Product>> createProducts() {
		Product productOne = new Product();
		productOne.setId(10L);
		productOne.setName("Raspberry PI");
		Price currentPrice = new Price();
		currentPrice.setCurrency(Currency.EUR);
		currentPrice.setValue(new BigDecimal("34"));
		productOne.setCurrentPrice(currentPrice);
		productOne.setColor(Color.BLACK);

		Product productTwo = new Product();
		productTwo.setId(20L);
		productTwo.setName("Arduino");
		Price price = new Price();
		price.setCurrency(Currency.EUR);
		price.setValue(new BigDecimal("44"));
		productTwo.setCurrentPrice(price);
		productTwo.setColor(Color.WHITE);

		return Arrays.asList(Optional.of(productOne), Optional.of(productTwo));
	}

	private static String convertObjectToJsonBytes(Object object)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(object);
	}
}
