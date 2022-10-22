package com.andrewsha.marketplace.domain.product_card;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.andrewsha.marketplace.domain.product.Product;
import com.andrewsha.marketplace.domain.store.Store;
import com.andrewsha.marketplace.security.IBinaryTree;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product_cards")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductCard implements Serializable, IBinaryTree {
    private static final long serialVersionUID = -521228307643576282L;
    @Id
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", updatable = true, nullable = false)
    @NotBlank(message = "name cannot be empty or null")
    @Size(min = 3, max = 20,
            message = "product name must be greater than 3 but less than 20 characters")
    private String name;

    @Column(name = "short_description", updatable = true, nullable = true)
    @Size(min = 10, max = 50,
            message = "short description must be greater than 10 but less than 50 characters")
    private String shortDescription;

    @Column(name = "description", updatable = true, nullable = true)
    @Size(min = 10, max = 100,
            message = "description must be greater than 10 but less than 100 characters")
    private String description;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "product_card_products", joinColumns = @JoinColumn(name = "product_card_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @NotEmpty(message = "set of products cannot be empty")
    private Set<Product> products = new HashSet<>();

    @Column(name = "category", updatable = true, nullable = false)
    @NotBlank(message = "category cannot be empty or null")
    private String category;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @NotNull(message = "store cannot be null")
    private Store store;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void deleteProduct(Product product) {
        this.products.remove(product);
        product.getProductCards().remove(this);
    }

    public void deleteProducts() {
        for (Product product : this.products) {
            product.getProductCards().remove(this);
        }
        this.products.clear();
    }

    @JsonIgnore
    @Override
    public SimpleEntry<String, Serializable> getRoot() {
        return new SimpleEntry<>("GLOBAL", null);
    }

    @JsonIgnore
    @Override
    public SimpleEntry<String, Serializable> getParent() {
        return new SimpleEntry<>(Store.class.getSimpleName(), this.store.getId());
    }
}
