package cz.muni.fi.priceService.controller;

import cz.muni.fi.priceService.entity.Price;
import cz.muni.fi.priceService.exception.ResourceNotFoundException;
import cz.muni.fi.priceService.repository.PriceRepository;
import cz.muni.fi.priceService.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

/**
 * REST Controller for priceService
 */
@RestController
@RequestMapping("/price")
public class PriceController {
    final static Logger logger = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private PriceService priceService;

    /**
     * Getting price according to id
     * Be aware that Price.historicalPrices are NOT NULL ONLY for prices associated with product as current price
     * and all others (generated historical prices) are NULL
     *
     * @param id price identifier
     * @return Price
     * @throws ResourceNotFoundException HTTP Status 404
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
