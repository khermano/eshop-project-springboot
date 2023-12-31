package cz.muni.fi.userservice.controller;

import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController usersController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(usersController).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void getAllUsers() throws Exception {
        doReturn(Collections.unmodifiableList(this.createUsers())).when(userRepository).findAll();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==1)].surname").value("Smith"))
                .andExpect(jsonPath("$.[?(@.id==2)].surname").value("Williams"));
    }

    @Test
    public void getValidUser() throws Exception {
        List<Optional<User>> users = this.createUsers();

        doReturn(users.get(0)).when(userRepository).findById(1L);
        doReturn(users.get(1)).when(userRepository).findById(2L);

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.surname").value("Smith"));

        mockMvc.perform(get("/2"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.surname").value("Williams"));
    }

    @Test
    public void getInvalidUser() throws Exception {
        doReturn(Optional.empty()).when(userRepository).findById(1L);

        mockMvc.perform(get("/1"))
                .andExpect(status().is4xxClientError());
    }

    private List<Optional<User>> createUsers() {
        User userOne = new User();
        userOne.setId(1L);
        userOne.setGivenName("John");
        userOne.setSurname("Smith");

        User userTwo = new User();
        userTwo.setId(2L);
        userTwo.setGivenName("Mary");
        userTwo.setSurname("Williams");
        
        return Arrays.asList(Optional.of(userOne), Optional.of(userTwo));
    }
}