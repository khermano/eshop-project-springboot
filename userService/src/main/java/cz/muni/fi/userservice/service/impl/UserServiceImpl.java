package cz.muni.fi.userservice.service.impl;

import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.repository.UserRepository;
import cz.muni.fi.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Implementation of the {@link UserService}. This class is part of the service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(User u, String unencryptedPassword) {
        u.setPasswordHash(passwordEncoder.encode(unencryptedPassword));
        userRepository.save(u);
    }

    @Override
    public boolean authenticate(User u, String password) {
        return passwordEncoder.matches(password, u.getPasswordHash());
    }

    @Override
    public boolean isAdmin(User u) {
        Optional<User> user = userRepository.findById(u.getId());
        if (user.isPresent()) {
            return user.get().isAdmin();
        } else {
            throw new IllegalArgumentException("Can't find user because of invalid id");
        }
    }
}