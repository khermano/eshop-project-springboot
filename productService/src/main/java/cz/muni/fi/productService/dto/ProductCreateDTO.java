package cz.muni.fi.productService.dto;

import cz.muni.fi.productService.enums.Color;
import cz.muni.fi.productService.enums.Currency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductCreateDTO {
    private byte[] image;

    private String imageMimeType;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Size(min = 3, max = 500)
    private String description;

    private Color color;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotNull
    private Currency currency;

    @NotNull
    private Long categoryId;

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
        ProductCreateDTO other = (ProductCreateDTO) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ProductCreateDTO{" +
                ", imageMimeType='" + imageMimeType + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color=" + color +
                ", price=" + price +
                ", currency=" + currency +
                '}';
    }
}
