package cz.muni.fi.productService.dto;

import cz.muni.fi.productService.entity.Price;
import cz.muni.fi.productService.enums.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

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

    private List<Price> priceHistory = new ArrayList<>();

    @Getter() @Setter()
    private Price currentPrice;
    
    public Set<CategoryDTO> getCategories() {
        return categories;
    }
    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    public List<Price> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory( List<Price> priceHistory) {
        this.priceHistory=priceHistory;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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
