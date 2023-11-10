package cz.muni.fi.categoryservice.sampledata;

import cz.muni.fi.categoryservice.entity.Category;
import cz.muni.fi.categoryservice.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleDataLoading {
    final static Logger log = LoggerFactory.getLogger(SampleDataLoading.class);

    @Autowired
    private CategoryRepository categoryRepository;

    private void createCategory(String name) {
        Category c = new Category();
        c.setName(name);
        categoryRepository.save(c);
    }

    @PostConstruct
    public void loadCategorySampleData() {
        createCategory("Food");
        createCategory("Office");
        createCategory("Flowers");
        createCategory("Toys");
        createCategory("Presents");

        log.info("Loaded eShop categories.");
    }
}
