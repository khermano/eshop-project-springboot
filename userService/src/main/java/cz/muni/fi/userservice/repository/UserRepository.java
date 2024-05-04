package cz.muni.fi.userservice.repository;

import cz.muni.fi.userservice.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
