package cz.muni.fi.userservice.service;

import cz.muni.fi.userservice.entity.User;

/**
 * An interface that defines a service access to the {@link User} entity.
 */
public interface UserService {
    /**
     * Register the given user with the given unencrypted password.
     */
    void registerUser(User u, String unencryptedPassword);

    /**
     * Try to authenticate a user. Return true only if the hashed password matches the records.
     */
    boolean authenticate(User u, String password);

    /**
     * Check if the given user is admin.
     */
    boolean isAdmin(User u);
}