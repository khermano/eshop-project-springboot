package cz.fi.muni.userservice.persistence.dao;

import cz.fi.muni.userservice.persistence.entity.User;

import java.util.List;

public interface UserDao {
	 public void create(User u);
	 public User findById(Long id);
	 public User findUserByEmail(String email);
	 public  List<User> findAll();
}
