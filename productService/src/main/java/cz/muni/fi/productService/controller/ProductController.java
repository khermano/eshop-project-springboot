package cz.muni.fi.productService.controller;

import cz.muni.fi.productService.dto.CategoryDTO;
import cz.muni.fi.productService.dto.ProductCreateDTO;
import cz.muni.fi.productService.dto.ProductDTO;
import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Currency;
import cz.muni.fi.productService.exception.EshopServiceException;
import cz.muni.fi.productService.feign.CategoryInterface;
import cz.muni.fi.productService.repository.ProductRepository;
import cz.muni.fi.productService.service.BeanMappingService;
import cz.muni.fi.productService.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST Controller for Products
 */
@RestController
public class ProductController {
    final static Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BeanMappingService beanMappingService;

    @Autowired
    private CategoryInterface categoryInterface;

    /**
     * Get list of Products
     * curl -i -X GET
     * http://localhost:8083
     *
     * @return list of Products
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDTO>> getProducts() {
        logger.debug("rest getProducts()");

        try {
            List<ProductDTO> productDTOs = new ArrayList<>();
            for (Product product : productRepository.findAll()) {
                ProductDTO productDTO = beanMappingService.mapTo(product, ProductDTO.class);
                productDTO.setCategories(getCategoriesFromIds(product.getCategoriesId()));
                productDTOs.add(productDTO);
            }
            return new ResponseEntity<>(productDTOs, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Get Product by identifier id
     * curl -i -X GET
     * http://localhost:8083/1
     *
     * @param id identifier for a product
     * @return Product with given id
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("id") long id) {
        logger.debug("rest getProduct({})", id);

        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            ProductDTO productDTO = beanMappingService.mapTo(product.get(), ProductDTO.class);
            productDTO.setCategories(getCategoriesFromIds(product.get().getCategoriesId()));
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested resource was not found");
        }
    }

    /**
     * Delete one product by id
     * curl -i -X DELETE
     * http://localhost:8083/1
     *
     * @param id identifier for product
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteProduct(@PathVariable("id") long id) {
        logger.debug("rest deleteProduct({})", id);

        try {
            productRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested resource was not found");
        }
    }

    /**
     * Create a new product by POST method
     * curl -X POST -i -H "Content-Type: application/json" --data 
     * '{"name":"test","description":"test","color":"UNDEFINED","price":"200",
     * "currency":"CZK", "categoryId":"1"}' 
     * http://localhost:8083/create
     * 
     * @param productInfo ProductCreateDTO with required fields for creation
     * @return the created product
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateDTO productInfo) {
        logger.debug("rest createProduct()");

        try {
            Product product = beanMappingService.mapTo(productInfo, Product.class);
            Price price = new Price();
            price.setValue(productInfo.getPrice());
            price.setCurrency(productInfo.getCurrency());
            Date now = new Date();
            price.setPriceStart(now);
            product.setAddedDate(now);
            product.setCurrentPrice(price);
            product.addHistoricalPrice(price);
            product.addCategoryId(productInfo.getCategoryId());
            product = productService.createProduct(product);

            ProductDTO productDTO = beanMappingService.mapTo(product, ProductDTO.class);
            productDTO.setCategories(getCategoriesFromIds(product.getCategoriesId()));
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update the price for one product by PUT method
     * curl -X PUT -i -H
     * "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}'
     * http://localhost:8083/4
     *
     * @param id identified of the product to be updated
     * @param newPrice required fields as specified in Price (value and currency)
     * @return the updated product
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> changePrice(@PathVariable("id") long id, @RequestBody Price newPrice) {
        logger.debug("rest changePrice({})", id);

        try {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                productService.changePrice(product.get(), newPrice);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
            }
            product = productRepository.findById(id);
            if (product.isPresent()) {
                ProductDTO productDTO = beanMappingService.mapTo(product.get(), ProductDTO.class);
                productDTO.setCategories(getCategoriesFromIds(product.get().getCategoriesId()));
                return new ResponseEntity<>(productDTO, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
            }
         } catch (EshopServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Add a new category by POST Method
     * curl -X POST -i -H "Content-Type: application/json" --data '{"id":"5","name":"Presents"}'
     * http://localhost:8083/2/categories
     *
     * Be aware that categoryService must be running for this to work!
     *
     * @param id the identifier of the Product to have the Category added
     * @param category the category to be added
     * @return the updated product as defined by ProductDTO
     */
    @PostMapping(value = "/{id}/categories", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> addCategory(@PathVariable("id") long id, @RequestBody CategoryDTO category) {
        logger.debug("rest addCategory({})", id);

        try {
            productService.addCategory(id, category);
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                ProductDTO productDTO = beanMappingService.mapTo(product.get(), ProductDTO.class);
                productDTO.setCategories(getCategoriesFromIds(product.get().getCategoriesId()));
                return new ResponseEntity<>(productDTO, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Get product's current Price by identifier id
     * curl -i -X GET
     * http://localhost:8083/2/currentPrice
     *
     * (This method is not from the original project, it needed to be created for the
     * OrderService's getTotalPrice method, so the original functionality stays)
     *
     * @param id identifier for a product
     * @return current Price of Product with given id
     */
    @GetMapping(value = "/{id}/currentPrice", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Price> getProductPriceByProductId(@PathVariable("id") long id) {
        logger.debug("rest getProductPriceByProductId({})", id);

        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<Price>(product.get().getCurrentPrice(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested resource was not found");
        }
    }

    /**
     * Get currency rate for given currency pair
     * curl -i -X GET
     * http://localhost:8083/getCurrencyRate/CZK/EUR
     *
     * (This method is not from the original project, it needed to be created for the
     * OrderService's getTotalPrice method, so the original functionality stays)
     *
     * @param currency1 first currency of the pair
     * @param currency2 second currency of the pair
     * @return currency rate for given pair
     */
    @GetMapping(value = "getCurrencyRate/{currency1}/{currency2}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> getCurrencyRate(@PathVariable("currency1") Currency currency1,
                                                      @PathVariable("currency2") Currency currency2) {
        logger.debug("rest getCurrencyRate({}, {})", currency1, currency2);

        try {
            return new ResponseEntity<>(productService.getCurrencyRate(currency1, currency2), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested resource was not found");
        }
    }

    private Set<CategoryDTO> getCategoriesFromIds(Set<Long> categoriesId) {
        Set<CategoryDTO> categories = new HashSet<>();
        for (Long categoryId: categoriesId) {
            categories.add(categoryInterface.getCategory(categoryId).getBody());
        }
        return categories;
    }
}
