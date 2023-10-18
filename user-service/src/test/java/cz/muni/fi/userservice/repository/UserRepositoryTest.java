package cz.muni.fi.userservice.repository;

import cz.muni.fi.userservice.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Date;

@DataJpaTest
public class UserRepositoryTest{
	@Autowired
	private UserRepository userRepository;

	private User u1 ;

	private User u2;


	@BeforeEach
	public void createUsers() {
		u1 = new User();
		u2 = new User();

		u1.setGivenName("Filip");
		u1.setEmail("filip@fi.cz");
		u1.setJoinedDate(new Date());
		u1.setSurname("Filipovic");
		u1.setAddress("Brno");

		u2.setGivenName("Jirka");
		u2.setEmail("jirka@fi.cz");
		u2.setJoinedDate(new Date());
		u2.setSurname("Jirkovic");
        u2.setAddress("Praha");

		userRepository.save(u1);
		userRepository.save(u2);
	}

	@Test
	public void findByEmail() {
		Assertions.assertNotNull(userRepository.findByEmail("filip@fi.cz"));

	}

	@Test
	public void findByNonExistentEmail() {
		Assertions.assertNull(userRepository.findByEmail("asdfasdfasd"));
	}
}