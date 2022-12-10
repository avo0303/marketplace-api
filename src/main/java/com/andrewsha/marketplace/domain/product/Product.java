package com.andrewsha.marketplace.domain.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.andrewsha.marketplace.domain.product.image.ProductImage;
import com.andrewsha.marketplace.domain.product_card.ProductCard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@TypeDefs({@TypeDef(name = "json", typeClass = JsonStringType.class),
		@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "name", updatable = true, nullable = false)
	@NotBlank(message = "name cannot be empty")
	@Size(min = 3, max = 20,
			message = "product name must be greater than 3 but less than 20 characters")
	private String name;

	@Column(name = "actual_price", updatable = true, nullable = false)
	@NotNull(message = "price cannot be null")
	@Min(value = 1, message = "price must be greater than 1")
	private Double actualPrice;

	@Min(value = 0, message = "discount must be equal or greater than 0")
	@Max(value = 99, message = "discount must be equal or less than 99")
	@Column(name = "discount", updatable = true, nullable = true)
	private Double discount;

	@Valid
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@NotEmpty(message = "images list must be not empty")
	private List<ProductImage> images = new ArrayList<>();

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb", name = "attributes", updatable = true, nullable = true)
	private Map<String, String> attributes = new HashMap<>();

	@JsonIgnore
	@ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
	private Set<ProductCard> productCards = new HashSet<>();

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double price) {
		this.actualPrice = price;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getDiscountedPrice() {
		if (this.discount != null) {
			return this.actualPrice - this.actualPrice * (this.discount / 100);
		}
		return this.actualPrice;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		for (ProductImage productImage : images) {
			productImage.setProduct(this);
		}
		this.images = images;
	}

	public void addImageURL(String url) {
		this.images.add(new ProductImage(url, this));
	}

	public void addImage(ProductImage productImage) {
		this.images.add(productImage);
	}

	public Set<ProductCard> getProductCards() {
		return productCards;
	}

	public void setProductCards(Set<ProductCard> productCards) {
		this.productCards = productCards;
	}

	public void deleteProductCard(ProductCard productCard) {
		this.productCards.remove(productCard);
		productCard.getProducts().remove(this);
	}

	public void deleteProductCards() {
		for (ProductCard productCard : this.productCards) {
			productCard.getProducts().remove(this);
		}
		this.productCards.clear();
	}
}
