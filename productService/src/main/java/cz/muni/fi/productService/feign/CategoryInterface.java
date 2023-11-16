package cz.muni.fi.productService.feign;

import cz.muni.fi.productService.dto.CategoryDTO;
import cz.muni.fi.productService.exception.ResourceAlreadyExistingException;
import cz.muni.fi.productService.exception.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient("CATEGORIES")
public interface CategoryInterface {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CategoryDTO>> getCategories();
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") long id) throws ResourceNotFoundException;
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryInfo) throws ResourceAlreadyExistingException;
}
