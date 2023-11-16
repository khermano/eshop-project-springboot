package cz.muni.fi.orderService.feign;

import cz.muni.fi.orderService.dto.UserDTO;
import cz.muni.fi.orderService.exception.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Collection;

@FeignClient("USERS")
public interface UserInterface {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<UserDTO>> getUsers();

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDTO> getUser(@PathVariable("id") long id) throws ResourceNotFoundException;
}
