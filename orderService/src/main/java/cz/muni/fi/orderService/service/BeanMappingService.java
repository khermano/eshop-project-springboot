package cz.muni.fi.orderService.service;

public interface BeanMappingService {
    <T> T mapTo(Object u, Class<T> mapToClass);
}
