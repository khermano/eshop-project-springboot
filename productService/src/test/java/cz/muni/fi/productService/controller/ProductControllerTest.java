package cz.muni.fi.productService.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.productService.dto.CategoryDTO;
import cz.muni.fi.productService.dto.ProductCreateDTO;
import cz.muni.fi.productService.dto.ProductDTO;
import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Color;
import cz.muni.fi.productService.enums.Currency;
import cz.muni.fi.productService.feign.CategoryInterface;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	@Mock
    private CategoryInterface categoryInterface;

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
		mockMvc.perform(get("/"));
	}

	@Test
	public void getAllProducts() throws Exception {
		doReturn(Collections.unmodifiableList(this.createProducts())).when(productRepository).findAll();
		doReturn(getMockedProductDTOList().get(0)).when(beanMappingService).mapTo(createProducts().get(0), ProductDTO.class);
		doReturn(getMockedProductDTOList().get(1)).when(beanMappingService).mapTo(createProducts().get(1), ProductDTO.class);
		doReturn(new ResponseEntity<>(new CategoryDTO(), HttpStatus.OK)).when(categoryInterface).getCategory(1L);

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(
						jsonPath("$.[?(@.id==10)].name").value("Raspberry PI"))
				.andExpect(jsonPath("$.[?(@.id==20)].name").value("Arduino"));
	}

	@Test
	public void getValidProduct() throws Exception {
		doReturn(Optional.of(createProducts().get(0))).when(productRepository).findById(10L);
		doReturn(Optional.of(createProducts().get(1))).when(productRepository).findById(20L);
		doReturn(getMockedProductDTOList().get(0)).when(beanMappingService).mapTo(createProducts().get(0), ProductDTO.class);
		doReturn(getMockedProductDTOList().get(1)).when(beanMappingService).mapTo(createProducts().get(1), ProductDTO.class);
		doReturn(new ResponseEntity<>(new CategoryDTO(), HttpStatus.OK)).when(categoryInterface).getCategory(1L);

		mockMvc.perform(get("/10"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.name").value("Raspberry PI"));
		mockMvc.perform(get("/20"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.name").value("Arduino"));
	}

	@Test
	public void getInvalidProduct() throws Exception {
		doReturn(Optional.empty()).when(productRepository).findById(1L);

		mockMvc.perform(get("/1")).andExpect(
				status().is4xxClientError());
	}

	@Test
	public void deleteProduct() throws Exception {
		doReturn(true).when(productRepository).existsById(any(Long.class));

		mockMvc.perform(delete("/10"))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteProductNonExisting() throws Exception {
		doReturn(false).when(productRepository).existsById(any(Long.class));

		mockMvc.perform(delete("/20"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void createProduct() throws Exception {
		ProductCreateDTO productCreateDTO = new ProductCreateDTO();
		productCreateDTO.setPrice(BigDecimal.valueOf(200));
		productCreateDTO.setCurrency(Currency.CZK);
		productCreateDTO.setCategoryId(1L);

		Product mockedProduct = new Product();
		mockedProduct.setId(1L);
		mockedProduct.addCategoryId(1L);

		doReturn(mockedProduct).when(beanMappingService).mapTo(productCreateDTO, Product.class);
		doReturn(mockedProduct).when(productService).createProduct(any(Product.class));
		doReturn(new ProductDTO()).when(beanMappingService).mapTo(mockedProduct, ProductDTO.class);
		doReturn(new ResponseEntity<>(new CategoryDTO(), HttpStatus.OK)).when(categoryInterface).getCategory(1L);

		String json = convertObjectToJsonBytes(productCreateDTO);

		System.out.println(json);

		mockMvc.perform(
				post("/create").contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void updateProduct() throws Exception {
		Product product = new Product();
		product.addCategoryId(1L);

		doReturn(Optional.of(product)).when(productRepository).findById(10L);
		doNothing().when(productService).changePrice(any(Product.class), any(Price.class));
		doReturn(new ProductDTO()).when(beanMappingService).mapTo(product, ProductDTO.class);
		doReturn(new ResponseEntity<>(new CategoryDTO(), HttpStatus.OK)).when(categoryInterface).getCategory(1L);

		String json = convertObjectToJsonBytes(new Price());

		mockMvc.perform(
				put("/10").contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void addCategory() throws Exception {
		Product mockedProduct = new Product();
		mockedProduct.setId(10L);
		mockedProduct.addCategoryId(1L);

		doReturn(Optional.of(mockedProduct)).when(productRepository).findById(10L);
		doReturn(new ProductDTO()).when(beanMappingService).mapTo(mockedProduct, ProductDTO.class);
		doReturn(new ResponseEntity<>(new CategoryDTO(), HttpStatus.OK)).when(categoryInterface).getCategory(1L);

		String json = convertObjectToJsonBytes(new CategoryDTO());

		mockMvc.perform(
				post("/10/categories").contentType(
						MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

	private List<Product> createProducts() {
		Product productOne = new Product();
		productOne.setId(10L);
		productOne.setName("Raspberry PI");
		Price currentPrice = new Price();
		currentPrice.setCurrency(Currency.EUR);
		currentPrice.setValue(new BigDecimal("34"));
		productOne.setCurrentPrice(currentPrice);
		productOne.setColor(Color.BLACK);
		productOne.addCategoryId(1L);

		Product productTwo = new Product();
		productTwo.setId(20L);
		productTwo.setName("Arduino");
		Price price = new Price();
		price.setCurrency(Currency.EUR);
		price.setValue(new BigDecimal("44"));
		productTwo.setCurrentPrice(price);
		productTwo.setColor(Color.WHITE);
		productTwo.addCategoryId(1L);

		return Arrays.asList(productOne, productTwo);
	}

	private static String convertObjectToJsonBytes(Object object)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(object);
	}

	private List<ProductDTO> getMockedProductDTOList() {
		ProductDTO mockedProductDTO = new ProductDTO();
		mockedProductDTO.setId(10L);
		mockedProductDTO.setName("Raspberry PI");

		ProductDTO mockedProductDTO2 = new ProductDTO();
		mockedProductDTO2.setId(20L);
		mockedProductDTO2.setName("Arduino");

		return Arrays.asList(mockedProductDTO, mockedProductDTO2);
	}
}
