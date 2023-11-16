package cz.muni.fi.productService.controller;

import cz.muni.fi.productService.dto.CategoryDTO;
import cz.muni.fi.productService.dto.ProductCreateDTO;
import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
import cz.muni.fi.productService.enums.Currency;
import cz.muni.fi.productService.exception.EshopServiceException;
import cz.muni.fi.productService.exception.InvalidParameterException;
import cz.muni.fi.productService.exception.ResourceAlreadyExistingException;
import cz.muni.fi.productService.exception.ResourceNotFoundException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Products
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    final static Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BeanMappingService beanMappingService;

    /**
     * Get list of Products
     * curl -i -X GET
     * http://localhost:8083/eshop-rest/products
     *
     * @return list of Products
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getProducts() {
        logger.debug("rest getProducts()");

        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Get Product by identifier id
     * curl -i -X GET
     * http://localhost:8083/eshop-rest/products/1
     *
     * @param id identifier for a product
     * @return Product with given id
     * @throws ResourceNotFoundException if product with given id does not exist
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProduct(@PathVariable("id") long id) throws ResourceNotFoundException {
        logger.debug("rest getProduct({})", id);

        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Delete one product by id
     * curl -i -X DELETE
     * http://localhost:8083/eshop-rest/products/1
     *
     * @param id identifier for product
     * @throws ResourceNotFoundException if for some reason we fail to delete product with given id
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteProduct(@PathVariable("id") long id) throws ResourceNotFoundException {
        logger.debug("rest deleteProduct({})", id);

        try {
            productRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Create a new product by POST method
     * curl -X POST -i -H "Content-Type: application/json" --data 
     * '{"name":"test","description":"test","color":"UNDEFINED","price":"200",
     * "currency":"CZK", "categoryId":"1"}' 
     * http://localhost:8083/eshop-rest/products/create
     * 
     * @param productInfo ProductCreateDTO with required fields for creation
     * @return the created product
     * @throws ResourceAlreadyExistingException if for some reason we fail to create product with given info
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreateDTO productInfo) throws ResourceAlreadyExistingException {
        logger.debug("rest createProduct()");

        try {
            Product product = beanMappingService.mapTo(productInfo, Product.class);
            //map price DTO to entity
            Price price = new Price();
            price.setValue(productInfo.getPrice());
            price.setCurrency(productInfo.getCurrency());
            Date now = new Date();
            price.setPriceStart(now);
            product.setAddedDate(now);
            //set price on product entity
            product.setCurrentPrice(price);
            product.addHistoricalPrice(price);
            //add to category
            product.addCategoryId(productInfo.getCategoryId());
            //save product
            return new ResponseEntity<>(productService.createProduct(product), HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResourceAlreadyExistingException();
        }
    }

    /**
     * Update the price for one product by PUT method
     * curl -X PUT -i -H
     * "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}'
     * http://localhost:8083/eshop-rest/products/4
     *
     * @param id identified of the product to be updated
     * @param newPrice required fields as specified in Price (value and currency)
     * @return the updated product
     * @throws InvalidParameterException if for some reason we fail to update product price with given info
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> changePrice(@PathVariable("id") long id, @RequestBody Price newPrice) throws InvalidParameterException {
        logger.debug("rest changePrice({})", id);

        try {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                productService.changePrice(product.get(), newPrice);
            } else {
                throw new InvalidParameterException();
            }
            //needs fresh call from DB
            product = productRepository.findById(id);
            if (product.isPresent()) {
                return new ResponseEntity<>(product.get(), HttpStatus.OK);
            } else {
                throw new InvalidParameterException();
            }
        } catch (EshopServiceException e) {
            throw new InvalidParameterException();
        }
    }

    /**
     * Add a new category by POST Method
     * curl -X POST -i -H "Content-Type: application/json" --data '{"id":"6","name":"test"}'
     * http://localhost:8083/eshop-rest/products/2/categories
     *
     * Be aware that categoryService must be running for this to work!
     *
     * @param id the identifier of the Product to have the Category added
     * @param category the category to be added
     * @return the updated product as defined by ProductDTO
     * @throws InvalidParameterException if for some reason we fail to add new category to product
     */
    @PostMapping(value = "/{id}/categories", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> addCategory(@PathVariable("id") long id, @RequestBody CategoryDTO category) throws InvalidParameterException {
        logger.debug("rest addCategory({})", id);

        try {
            productService.addCategory(id, category);
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                return new ResponseEntity<>(product.get(), HttpStatus.OK);
            } else {
                throw new InvalidParameterException();
            }
        } catch (Exception ex) {
            throw new InvalidParameterException();
        }
    }

    /**
     * Get product's current Price by identifier id
     * curl -i -X GET
     * http://localhost:8083/eshop-rest/products/2/currentPrice
     *
     * (This method is not from the original project, it needed to be created for the
     * OrderService's getTotalPrice method, so the original functionality stays)
     *
     * @param id identifier for a product
     * @return current Price of Product with given id
     * @throws ResourceNotFoundException if product with given id does not exist
     */
    @GetMapping(value = "/{id}/currentPrice", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Price> getProductPriceByProductId(@PathVariable("id") long id) throws ResourceNotFoundException {
        logger.debug("rest getProductPriceByProductId({})", id);

        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(product.get().getCurrentPrice(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Get currency rate for given currency pair
     * curl -i -X GET
     * http://localhost:8083/eshop-rest/products/getCurrencyRate/CZK/EUR
     *
     * (This method is not from the original project, it needed to be created for the
     * OrderService's getTotalPrice method, so the original functionality stays)
     *
     * @param currency1 first currency of the pair
     * @param currency2 second currency of the pair
     * @return currency rate for given pair
     * @throws ResourceNotFoundException if given currency pair doesn't exist
     */
    @GetMapping(value = "getCurrencyRate/{currency1}/{currency2}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> getCurrencyRate(@PathVariable("currency1") Currency currency1, @PathVariable("currency2") Currency currency2) throws ResourceNotFoundException {
        logger.debug("rest getCurrencyRate({}, {})", currency1, currency2);

        try {
            return new ResponseEntity<>(productService.getCurrencyRate(currency1, currency2), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            throw new ResourceNotFoundException();
        }
    }
}
