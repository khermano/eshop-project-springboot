package cz.muni.fi.categoryservice.controller;

import cz.muni.fi.categoryservice.entity.Category;
import cz.muni.fi.categoryservice.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Categories
 * Methods are implemented in a way that they imitate the ones from the original project
 */
@RestController
public class CategoryController {
    final static Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get list of Categories
     *
     * example in categoryService:
     * curl -i -X GET http://localhost:8082
     *
     * @return list of Categories
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> getCategories() {
        logger.debug("rest getCategories()");

        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Get Category specified by ID
     *
     * example in categoryService:
     * curl -i -X GET http://localhost:8082/1
     * 
     * @param id identifier for the category
     * @return Category with given ID
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable("id") long id) {
        logger.debug("rest getCategory({})", id);

        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested resource was not found");
        }
    }
}
