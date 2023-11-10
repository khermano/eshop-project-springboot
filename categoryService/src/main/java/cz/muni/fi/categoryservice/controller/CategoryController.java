package cz.muni.fi.categoryservice.controller;

import cz.muni.fi.categoryservice.controller.exception.ResourceNotFoundException;
import cz.muni.fi.categoryservice.entity.Category;
import cz.muni.fi.categoryservice.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * get all the categories
     * @return list of CategoryDTOs
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<Category> getCategories() {
        logger.debug("rest getCategories()");

        return categoryRepository.findAll();
    }

    /**
     * Get one category specified by id
     * 
     * @param id identifier for the category
     * @return CategoryDTO
     * @throws ResourceNotFoundException ResourceNotFoundException
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
}
