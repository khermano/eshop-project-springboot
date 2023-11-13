package cz.muni.fi.categoryservice.controller;

import cz.muni.fi.categoryservice.exception.ResourceAlreadyExistingException;
import cz.muni.fi.categoryservice.exception.ResourceNotFoundException;
import cz.muni.fi.categoryservice.entity.Category;
import cz.muni.fi.categoryservice.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Categories
 *
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {
    final static Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get list of Categories
     * curl -i -X GET
     * http://localhost:8082/eshop-rest/categories
     *
     * @return list of Categories
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<Category> getCategories() {
        logger.debug("rest getCategories()");

        return categoryRepository.findAll();
    }

    /**
     * Get Category specified by ID
     * curl -i -X GET
     * http://localhost:8082/eshop-rest/categories/1
     * 
     * @param id identifier for the category
     * @return Category with given ID
     * @throws ResourceNotFoundException if category with given ID does not exist
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final Category getCategory(@PathVariable("id") long id) throws ResourceNotFoundException {
        logger.debug("rest getCategory({})", id);

        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Create a new product by POST method
     * curl -X POST -i -H "Content-Type: application/json" --data
     * '{"id":"6","name":"test"}'
     * http://localhost:8082/eshop-rest/categories/create
     *
     * (This method is not from the original project, it needed to be created for the
     * ProductService's addCategory method, so the original functionality stays)
     *
     * @param categoryInfo Category with required fields for creation
     * @return the created category
     * @throws ResourceAlreadyExistingException if for some reason we fail to create product with given info
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final Category createCategory(@RequestBody Category categoryInfo) throws ResourceAlreadyExistingException {
        logger.debug("rest createCategory()");

        Optional<Category> category = categoryRepository.findById(categoryInfo.getId());
        if (category.isPresent()) {
            throw new ResourceAlreadyExistingException();
        }
        if (categoryInfo.getId() == null || categoryInfo.getName() == null || categoryInfo.getName().isEmpty()) {
            throw new IllegalArgumentException();
        }

        try {
            categoryRepository.save(categoryInfo);
            return categoryRepository.findById(categoryInfo.getId()).get();
        } catch (Exception e) {
            throw new ResourceAlreadyExistingException();
        }
    }
}
