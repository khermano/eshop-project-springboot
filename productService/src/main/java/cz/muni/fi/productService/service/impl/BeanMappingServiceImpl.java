package cz.muni.fi.productService.service.impl;

import cz.muni.fi.productService.service.BeanMappingService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class BeanMappingServiceImpl implements BeanMappingService {
	@Bean
    private Mapper getDozer() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        return mapper;
    }

    @Override
    public <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass) {
        List<T> mappedCollection = new ArrayList<>();
        for (Object object : objects) {
            mappedCollection.add(getDozer().map(object, mapToClass));
        }
        return mappedCollection;
    }

    @Override
    public <T> T mapTo(Object u, Class<T> mapToClass)
    {
        return getDozer().map(u,mapToClass);
    }
}
