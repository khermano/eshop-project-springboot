package cz.muni.fi.orderService.dto;

import cz.muni.fi.orderService.enums.Color;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductDTO {
    @Getter() @Setter()
    private Long id;

    @Getter() @Setter()
    private String name;

    @Getter() @Setter()
    private String description;

    private Color color;

    @Getter() @Setter()
    private Date addedDate;

    private Set<CategoryDTO> categories = new HashSet<>();

    private List<PriceDTO> priceHistory = new ArrayList<>();

    private PriceDTO currentPrice;
    
    public Set<CategoryDTO> getCategories() {
        return categories;
    }
    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    public List<PriceDTO> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory( List<PriceDTO> priceHistory) {
        this.priceHistory=priceHistory;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setCurrentPrice(PriceDTO currentPrice) {
        this.currentPrice = currentPrice;
    }

    public PriceDTO getCurrentPrice() {
        return currentPrice;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductDTO other = (ProductDTO) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color=" + color +
                ", addedDate=" + addedDate +
                ", categories=" + categories +
                ", priceHistory=" + priceHistory +
                ", currentPrice=" + currentPrice +
                '}';
    }
}
