package cz.muni.fi.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.exception.ResourceNotFoundException;
import cz.muni.fi.userservice.repository.UserRepository;
import cz.muni.fi.userservice.service.UserService;
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
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    /**
     * returns all users
     *
     * @return list of Users
     * @throws JsonProcessingException
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("/list")
    public final Collection<User> getUsers() throws JsonProcessingException {
        
        logger.debug("rest getUsers()");
        return userService.getAllUsers();
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
    public final User getUser(@PathVariable("id") long id) throws Exception {
        logger.debug("rest getUser({})", id);
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        }
        else {
            throw new ResourceNotFoundException();
         }
    }
}
