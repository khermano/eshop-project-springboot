package cz.muni.fi.orderService.dto;

import cz.muni.fi.orderService.enums.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductDTO {
    private Long id;

    private String name;

    private String description;

    private Color color;

    private Date addedDate;

    private Set<CategoryDTO> categories = new HashSet<>();

    private List<PriceDTO> priceHistory = new ArrayList<>();

    private PriceDTO currentPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    public List<PriceDTO> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<PriceDTO> priceHistory) {
        this.priceHistory = priceHistory;
    }

    public PriceDTO getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(PriceDTO currentPrice) {
        this.currentPrice = currentPrice;
    }
}
