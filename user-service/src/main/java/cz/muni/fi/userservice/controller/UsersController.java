package cz.muni.fi.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.exception.ResourceNotFoundException;
import cz.muni.fi.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * REST Controller for Users
 *
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    
    final static Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserService userService;

    /**
     * returns all users
     *
     * @return list of Users
     * @throws JsonProcessingException
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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
         User user = userService.findUserById(id);
         if (user == null){
            throw new ResourceNotFoundException();
         }
         return user;
        

    }
}
