package cz.muni.fi.priceService.controller;

import cz.muni.fi.priceService.entity.Price;
import cz.muni.fi.priceService.exception.ResourceNotFoundException;
import cz.muni.fi.priceService.repository.PriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/price")
public class PriceController {
    final static Logger logger = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    private PriceRepository priceRepository;

    /**
     *
     * getting price according to id
     *
     * @param id price identifier
     * @return User
     * @throws ResourceNotFoundException
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final Price getPrice(@PathVariable("id") long id) {
        logger.debug("rest getPrice({})", id);
        Optional<Price> price = priceRepository.findById(id);
        if (price.isPresent()) {
            return price.get();
        }
        else {
            throw new ResourceNotFoundException();
        }
    }
}
