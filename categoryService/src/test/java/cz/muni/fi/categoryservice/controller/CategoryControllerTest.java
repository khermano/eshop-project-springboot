package cz.muni.fi.categoryservice.controller;

import cz.muni.fi.categoryservice.entity.Category;
import cz.muni.fi.categoryservice.repository.CategoryRepository;
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
public class CategoryControllerTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
       mockMvc = standaloneSetup(categoryController).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void getAllCategories() throws Exception {
        doReturn(Collections.unmodifiableList(this.createCategories())).when(categoryRepository).findAll();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[?(@.id==1)].name").value("Electronics"))
                .andExpect(jsonPath("$.[?(@.id==2)].name").value("Home Appliances"));
    }

    @Test
    public void getValidCategory() throws Exception {
        List<Optional<Category>> categories = this.createCategories();

        doReturn(categories.get(0)).when(categoryRepository).findById(1L);
        doReturn(categories.get(1)).when(categoryRepository).findById(2L);

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value("Electronics"));

        mockMvc.perform(get("/2"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value("Home Appliances"));
    }

    @Test
    public void getInvalidCategory() throws Exception {
        doReturn(Optional.empty()).when(categoryRepository).findById(1L);

        mockMvc.perform(get("/1"))
                .andExpect(status().is4xxClientError());
    }

    private List<Optional<Category>> createCategories() {
        Category catOne = new Category();
        catOne.setId(1L);
        catOne.setName("Electronics");

        Category catTwo = new Category();
        catTwo.setId(2L);
        catTwo.setName("Home Appliances");

        return Arrays.asList(Optional.of(catOne), Optional.of(catTwo));
    }
}
