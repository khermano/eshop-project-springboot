package cz.muni.fi.userservice.controller;

import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.exception.ResourceNotFoundException;
import cz.muni.fi.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;

/**
 * REST Controller for Users
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    final static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;

    /**
     * returns all users
     *
     * @return list of Users
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("/list")
    public final Collection<User> getUsers() {
        
        logger.debug("rest getUsers()");
        return userRepository.findAll();
    }

    /**
     *
     * getting user according to id
     * 
     * @param id user identifier
     * @return User
     * @throws ResourceNotFoundException
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final User getUser(@PathVariable("id") long id) {
        logger.debug("rest getUser({})", id);
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        }
        else {
            throw new ResourceNotFoundException();
         }
    }
}
