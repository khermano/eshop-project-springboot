package cz.muni.fi.productService.service;

public interface BeanMappingService {
    <T> T mapTo(Object u, Class<T> mapToClass);
}
