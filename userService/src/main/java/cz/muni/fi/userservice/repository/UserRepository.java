package cz.muni.fi.userservice.repository;

import cz.muni.fi.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	List<User> findByAdminTrue();
}
