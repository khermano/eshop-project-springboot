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
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllOrNothing(members={"image", "imageMimeType"})
@Data
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
}
