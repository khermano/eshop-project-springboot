package cz.muni.fi.orderService.feign;

import cz.muni.fi.orderService.exception.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("USERS")
public interface UserInterface {
    @GetMapping(value = "users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getUser(@PathVariable("id") long id) throws ResourceNotFoundException;
}
