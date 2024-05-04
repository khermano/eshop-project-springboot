package cz.muni.fi.userservice.sampledata;

import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class SampleDataLoadingTest {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoadingTest.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createSampleData() {
        log.debug("Starting test");

        List<User> users = (List<User>)userRepository.findAll();
        Assertions.assertFalse(users.isEmpty());
    }
}
