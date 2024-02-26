package cz.muni.fi.productService.controller;

import cz.muni.fi.productService.dto.CategoryDTO;
import cz.muni.fi.productService.dto.NewPriceDTO;
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
 * Methods are implemented in a way that they imitate the ones from the original project
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
     * Get list of all Products
     *
     * example in productService:
     * curl -i -X GET http://localhost:8083
     *
     * @return list of all Products
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDTO>> getProducts() {
        logger.debug("rest getProducts()");

        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            ProductDTO productDTO = beanMappingService.mapTo(product, ProductDTO.class);
            productDTO.setCategories(getCategoriesFromIds(product.getCategoriesId()));
            productDTOs.add(productDTO);
        }
        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
    }

    /**
     * Get Product by identifier id
     *
     * example in productService:
     * curl -i -X GET http://localhost:8083/1
     *
     * @param id identifier for a product
     * @return Product with given id
     * @throws ResponseStatusException 404 if product with given ID doesn't exist
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
     * Delete one product by ID
     *
     * example in productService:
     * curl -i -X DELETE http://localhost:8083/1
     *
     * @param id identifier for product
     * @throws ResponseStatusException 404 if product with given ID doesn't exist
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteProduct(@PathVariable("id") long id) {
        logger.debug("rest deleteProduct({})", id);

        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested resource was not found");
        }
    }

    /**
     * Create a new product by POST method
     *
     * example in productService:
     * curl -X POST -i -H "Content-Type: application/json" --data '{"name":"test","description":"test","color":"UNDEFINED","price":"200",
     * "currency":"CZK", "categoryId":"1"}' http://localhost:8083/create
     * 
     * @param productInfo ProductCreateDTO with required fields for creation
     *                    (name, description, price, currency, categoryId can't be null)
     *                    you either fill both image and imageType or none of them (see @AllOrNothing)
     * @return the created product
     * @throws ResponseStatusException 422 when invalid data provided or anything went wrong
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
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "the requested resource already exists");
        }
    }

    /**
     * Update the price for one product by PUT method
     *
     * example in productService:
     * curl -X PUT -i -H "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}' http://localhost:8083/4
     *
     * @param id identified of the product to be updated
     * @param newPrice need only value and currency of new price
     * @return the updated product
     * @throws ResponseStatusException 500 if there is no product with given ID
     * @throws ResponseStatusException 406 if value of price is changed more than 10% or something else went wrong
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> changePrice(@PathVariable("id") long id, @RequestBody NewPriceDTO newPrice) {
        logger.debug("rest changePrice({})", id);

        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            productService.changePrice(product.get(), newPrice);
        } catch (EshopServiceException e) {
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
    }

    /**
     * Add a new category by POST Method
     *
     * example in productService:
     * curl -X POST -i -H "Content-Type: application/json" --data '{"id":"5","name":"Presents"}' http://localhost:8083/2/categories
     *
     * Be aware that categoryService must be running for this to work!
     *
     * @param id the identifier of the Product to have the Category added
     * @param categoryId id of category to be added
     *                 we need only ID of existing category we want to add
     * @return the updated product as defined by ProductDTO
     * @throws ResponseStatusException 406 if something went wrong
     */
    @PostMapping(value = "/{id}/categories", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> addCategory(@PathVariable("id") long id, @RequestBody long categoryId) {
        logger.debug("rest addCategory({})", id);

        try {
            productService.addCategory(id, categoryId);
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
     * Get product's current Price by identifier ID
     * (This method is not from the original project, it needed to be created for the
     * OrderService's getTotalPrice method, so the original functionality stays)
     *
     * example in productService:
     * curl -i -X GET http://localhost:8083/2/currentPrice
     *
     * @param id identifier for a product
     * @return current Price of Product with given ID
     * @throws ResponseStatusException 404 if product with given ID doesn't exist
     */
    @GetMapping(value = "/{id}/currentPrice", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Price> getProductPriceByProductId(@PathVariable("id") long id) {
        logger.debug("rest getProductPriceByProductId({})", id);

        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(product.get().getCurrentPrice(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested resource was not found");
        }
    }

    /**
     * Get currency rate for given currency pair
     * (This method is not from the original project, it needed to be created for the
     * OrderService's getTotalPrice method, so the original functionality stays)
     *
     * example in productService:
     * curl -i -X GET http://localhost:8083/getCurrencyRate/CZK/EUR
     *
     * @param currency1 first currency of the pair
     * @param currency2 second currency of the pair
     * @return currency rate for given pair
     * @throws ResponseStatusException 404 if given currency pair doesn't exist (see pairs in ProductServiceImpl)
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
