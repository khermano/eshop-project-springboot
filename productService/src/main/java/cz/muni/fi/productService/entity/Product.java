package cz.muni.fi.productService.entity;

import cz.fi.muni.pa165.enums.Color;
import cz.fi.muni.pa165.validation.AllOrNothing;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@AllOrNothing(members={"image", "imageMimeType"})
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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

    /*
	 * The day this item has been added to the eshop
	 */
	@Temporal(TemporalType.DATE)
	private Date addedDate;
	
	@ManyToMany
	private Set<Category> categories = new HashSet<Category>();


	@OneToOne
	@JoinTable(name="CURRENT_PRICE")
	private Price currentPrice;
	
	@OneToMany()
	@OrderBy("priceStart DESC")
	@JoinColumn(name="Product_FK")
	private List<Price> priceHistory = new ArrayList<Price>();
	
	public void setId(Long id){
		this.id = id;
	}
	
	public void removeCategory(Category category)
	{
		this.categories.remove(category);
	}

	public Set<Category> getCategories() {
		return Collections.unmodifiableSet(categories);
	}
	
	public void addCategory(Category c) {
		categories.add(c);
		c.addProduct(this);
	}


	public Date getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}
	public Product(Long productId) {
		this.id = productId;
	}
	public Product() {
	}
	public byte[] getImage() {
		return image;
	}
	

	public String getImageMimeType() {
		return imageMimeType;
	}



	public void setImageMimeType(String imageMimeType) {
		this.imageMimeType = imageMimeType;
	}



	public Price getCurrentPrice() {
		return currentPrice;
	}


	public void addHistoricalPrice(Price p){
		priceHistory.add(p);
	}
	
	public void setCurrentPrice(Price currentPrice) {
		this.currentPrice = currentPrice;
	}


	public List<Price> getPriceHistory() {
		return Collections.unmodifiableList(priceHistory);
	}


	public void setImage(byte[] image) {
		this.image = image;
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


	public Long getId() {
		return id;
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
                ", categories=" + categories +
                ", currentPrice=" + currentPrice +
                ", priceHistory=" + priceHistory +
                ", color=" + color +
                '}';
    }
}
