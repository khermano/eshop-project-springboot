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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
		mockMvc.perform(get("/products"));
	}

	@Test
	public void getAllProducts() throws Exception {
		doReturn(Collections.unmodifiableList(this.createProducts())).when(
				productRepository).findAll();

		mockMvc.perform(get("/products"))
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

		doReturn(products.get(0)).when(productRepository).findById(10L);
		doReturn(products.get(1)).when(productRepository).findById(20L);

		mockMvc.perform(get("/products/10"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.name").value("Raspberry PI"));
		mockMvc.perform(get("/products/20"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.name").value("Arduino"));
	}

	@Test
	public void getInvalidProduct() throws Exception {
		doReturn(Optional.empty()).when(productRepository).findById(1L);

		mockMvc.perform(get("/products/1")).andExpect(
				status().is4xxClientError());
	}

	@Test
	public void deleteProduct() throws Exception {
		mockMvc.perform(delete("/products/10"))
				.andExpect(status().isOk());
	}
        
	@Test
	public void deleteProductNonExisting() throws Exception {
		doThrow(new RuntimeException("the product does not exist")).when(productRepository).deleteById(20L);

		mockMvc.perform(delete("/products/20"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void createProduct() throws Exception {
		ProductCreateDTO productCreateDTO = new ProductCreateDTO();

		Product mockedProduct = new Product();
		mockedProduct.setId(1L);

		doReturn(mockedProduct).when(beanMappingService).mapTo(productCreateDTO, Product.class);
		doReturn(mockedProduct).when(productService).createProduct(any(Product.class));

		String json = convertObjectToJsonBytes(productCreateDTO);

		System.out.println(json);

		mockMvc.perform(
				post("/products/create").contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void updateProduct() throws Exception {
		List<Optional<Product>> products = this.createProducts();

		doReturn(products.get(0)).when(productRepository).findById(10L);

		doNothing().when(productService).changePrice(any(Product.class), any(Price.class));
		Price newPrice = new Price();
		
		String json = convertObjectToJsonBytes(newPrice);

		mockMvc.perform(
				put("/products/10").contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void addCategory() throws Exception {
		List<Optional<Product>> products = this.createProducts();

		doReturn(products.get(0)).when(productRepository).findById(10L);

		Long categoryId = 1L;

		String json = convertObjectToJsonBytes(categoryId);

		mockMvc.perform(
				post("/products/10/categories").contentType(
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
