package cz.muni.fi.productService.feign;

import cz.muni.fi.productService.dto.CategoryDTO;
import cz.muni.fi.productService.exception.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CATEGORIES")
public interface CategoryInterface {
    @GetMapping(value = "categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") long id) throws ResourceNotFoundException;
}
