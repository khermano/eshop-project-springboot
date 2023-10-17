package cz.muni.fi.userservice.service.impl;

import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.repository.UserRepository;
import cz.muni.fi.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link UserService}. This class is part of the service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    // Argon2 is the best Key Derivation Function since 2015
    //private final PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

//    @Autowired
//    public PasswordEncoder passwordEncoder() {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return encoder;
//    }

    @Override
    public void registerUser(User u, String unencryptedPassword) {
        u.setPasswordHash("Xxxxxxxxxx"/*passwordEncoder().encode(unencryptedPassword)*/);
        userRepository.save(u);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean authenticate(User u, String password) {
        return true;//passwordEncoder().matches(password, u.getPasswordHash());
    }

    @Override
    public boolean isAdmin(User u) {
        //must get a fresh copy from database
        return findUserById(u.getId()).isAdmin();
    }

    @Override
    public User findUserById(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            return userRepository.findById(userId).get();
        } else {
            throw new IllegalArgumentException("Can't find user because of invalid id");
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
