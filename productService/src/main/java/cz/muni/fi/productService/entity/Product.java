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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllOrNothing(members={"image", "imageMimeType"})
public class Product {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Lob
	private byte[] image;

	private String imageMimeType;
	
	@NotNull
	@Column(nullable=false,unique=true)
	private String name;

	private String description;

    @Enumerated
    private Color color;

    /**
	 * The day this item has been added to the eshop
	 */
	@Temporal(TemporalType.DATE)
	private Date addedDate;

	@ElementCollection
	private Set<Long> categoriesId = new HashSet<>();

	@OneToOne
	@JoinTable(name="CURRENT_PRICE")
	private Price currentPrice;

	/**
	 * For sample data it's randomly generated when application starts
	 */
	@OneToMany(fetch = FetchType.EAGER)
	@OrderBy("priceStart DESC")
	@JoinColumn(name="Product_FK")
	private List<Price> priceHistory = new ArrayList<>();

	public void addCategoryId(Long categoryId) {
		categoriesId.add(categoryId);
	}

	public void addHistoricalPrice(Price p){
		priceHistory.add(p);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageMimeType() {
		return imageMimeType;
	}

	public void setImageMimeType(String imageMimeType) {
		this.imageMimeType = imageMimeType;
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

	public Set<Long> getCategoriesId() {
		return categoriesId;
	}

	public void setCategoriesId(Set<Long> categoriesId) {
		this.categoriesId = categoriesId;
	}

	public Price getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Price currentPrice) {
		this.currentPrice = currentPrice;
	}

	public List<Price> getPriceHistory() {
		return priceHistory;
	}

	public void setPriceHistory(List<Price> priceHistory) {
		this.priceHistory = priceHistory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		return prime * result + ((name == null) ? 0 : name.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Product other)) {
			return false;
		}
		if (name == null) {
            return other.name == null;
		} else return name.equals(other.getName());
    }
}
