package cz.muni.fi.productService.entity;

import cz.muni.fi.productService.validation.AllOrNothing;
import cz.muni.fi.productService.enums.Color;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllOrNothing(members={"image", "imageMimeType"})
public class Product {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;

	@Lob
	@Getter @Setter
	private byte[] image;

	@Setter
	private String imageMimeType;
	
	@NotNull
	@Column(nullable=false,unique=true)
	@Getter @Setter
	private String name;

	@Getter @Setter
	private String description;

    @Enumerated
    private Color color;

    /**
	 * The day this item has been added to the eshop
	 */
	@Temporal(TemporalType.DATE)
	@Getter @Setter
	private Date addedDate;

	@ElementCollection
	private Set<Long> categoriesId = new HashSet<>();

	@OneToOne
	@JoinTable(name="CURRENT_PRICE")
	@Getter @Setter
	private Price currentPrice;

	/**
	 * It's randomly generated when application starts
	 */
	@OneToMany(fetch = FetchType.EAGER)
	@OrderBy("priceStart DESC")
	@JoinColumn(name="Product_FK")
	private List<Price> priceHistory = new ArrayList<>();

	public Set<Long> getCategoriesId() {
		return Collections.unmodifiableSet(categoriesId);
	}
	
	public void addCategoryId(Long categoryId) {
		categoriesId.add(categoryId);
	}

	public void addHistoricalPrice(Price p){
		priceHistory.add(p);
	}

	public List<Price> getPriceHistory() {
		return Collections.unmodifiableList(priceHistory);
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
		if (!(obj instanceof Product))
			return false;
		Product other = (Product) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", addedDate=" + addedDate +
                ", categoriesId=" + categoriesId +
                ", currentPrice=" + currentPrice +
                ", priceHistory=" + priceHistory +
                ", color=" + color +
                '}';
    }
}
