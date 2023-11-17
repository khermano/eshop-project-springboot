package cz.muni.fi.userservice.controller;

import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.exception.ResourceNotFoundException;
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
     * curl -i -X GET
     * http://localhost:8081
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
     * curl -i -X GET
     * http://localhost:8081/1
     * 
     * @param id user identifier
     * @return User
     * @throws ResourceNotFoundException HTTP Status 404
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") long id) throws ResourceNotFoundException {
        logger.debug("rest getUser({})", id);

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else {
            throw new ResourceNotFoundException();
         }
    }
}
