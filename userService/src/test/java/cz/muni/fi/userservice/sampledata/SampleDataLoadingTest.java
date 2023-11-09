package cz.muni.fi.userservice.sampledata;

import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.repository.UserRepository;
import cz.muni.fi.userservice.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

@SpringBootTest
public class SampleDataLoadingTest {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoadingTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createSampleData() {
        log.debug("Starting test");

        Optional<User> admin = userRepository.findAll().stream().filter(userService::isAdmin).findFirst();
        Assertions.assertTrue(admin.isPresent());
        Assertions.assertTrue(userService.authenticate(admin.get(), "admin"));
    }
}
