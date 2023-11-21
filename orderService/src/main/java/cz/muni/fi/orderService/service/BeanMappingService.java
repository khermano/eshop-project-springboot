package cz.muni.fi.orderService.service;

import java.util.Collection;
import java.util.List;

public interface BeanMappingService {
    <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);
    <T> T mapTo(Object u, Class<T> mapToClass);
}
