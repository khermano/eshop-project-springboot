package cz.muni.fi.userservice.controller;

import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.repository.UserRepository;
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
import java.util.Collection;
import java.util.Optional;

/**
 * REST Controller for Users
 */
@RestController
public class UserController {
    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Returns all users
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/users
     *
     * @return list of Users
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<User>> getUsers() {
        logger.debug("rest getUsers()");

        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Getting user according to id
     * e.g.: curl -i -X GET http://localhost:8080/eshop-rest/users/1
     * 
     * @param id of the user
     * @return User with given id
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        logger.debug("rest getUser({})", id);

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested resource was not found");
         }
    }
}
