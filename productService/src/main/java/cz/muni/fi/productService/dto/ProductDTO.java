package cz.muni.fi.productService.dto;

import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.enums.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;

    private String name;

    private String description;

    private Color color;

    private Date addedDate;

    private Set<CategoryDTO> categories = new HashSet<>();

    private List<Price> priceHistory = new ArrayList<>();

    private Price currentPrice;
}
