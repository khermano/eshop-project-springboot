package cz.muni.fi.productService.controller;

import cz.muni.fi.productService.dto.CategoryDTO;
import cz.muni.fi.productService.dto.ProductCreateDTO;
import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.entity.Product;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<Product> getProducts() {
        logger.debug("rest getProducts()");

        return productRepository.findAll();
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
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final Product getProduct(@PathVariable("id") long id) throws ResourceNotFoundException {
        logger.debug("rest getProduct({})", id);

        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
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
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void deleteProduct(@PathVariable("id") long id) throws ResourceNotFoundException {
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
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final Product createProduct(@RequestBody ProductCreateDTO productInfo) throws ResourceAlreadyExistingException {
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
            return productService.createProduct(product);
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
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final Product changePrice(@PathVariable("id") long id, @RequestBody Price newPrice) throws InvalidParameterException {
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
                return product.get();
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
    @RequestMapping(value = "/{id}/categories", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final Product addCategory(@PathVariable("id") long id, @RequestBody CategoryDTO category) throws InvalidParameterException, IOException {
        logger.debug("rest addCategory({})", id);

        try {
            productService.addCategory(id, category);
            Optional<Product> product = productRepository.findById(id);
            return product.get();
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
    @RequestMapping(value = "/{id}/currentPrice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final Price getProductPriceByProductId(@PathVariable("id") long id) throws ResourceNotFoundException {
        logger.debug("rest getProductPriceByProductId({})", id);

        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get().getCurrentPrice();
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
