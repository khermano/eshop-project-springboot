package cz.muni.fi.orderService.service.impl;

import cz.muni.fi.orderService.service.BeanMappingService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class BeanMappingServiceImpl implements BeanMappingService {
	@Bean
    private Mapper getDozer() {
        return new DozerBeanMapper();
    }

    @Override
    public <T> T mapTo(Object u, Class<T> mapToClass)
    {
        return getDozer().map(u,mapToClass);
    }
}
