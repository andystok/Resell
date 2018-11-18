package cards.resell.products;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cards.resell.products.attributes.Attribute;
import cards.resell.products.attributes.AttributeValue;
import cards.resell.products.images.Image;
import cards.resell.products.tags.Tag;
import cards.resell.products.versions.Version;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@NotBlank
	@Column(name ="productName")
	private String productName;

	@ManyToMany
    @JoinTable(name = "product_tags",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
	
	@ManyToMany
    @JoinTable(name = "product_versions",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "version_id")
    )
    private Set<Version> versions = new HashSet<>();
	
	@ManyToMany
    @JoinTable(name = "product_attribute_values",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "attribute_value_id") 
    )
    private Map<Attribute, AttributeValue> attributes = new HashMap<>();
	
	@OneToMany(
		mappedBy = "product",
        cascade = CascadeType.ALL, 
        orphanRemoval = true
    )
    private Set<Image> images = new HashSet<>();
	
	@Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
    
    private String description;

    public Product() {}
	 
    public Product(String name) {
        this.productName = name;
    }
    
    public void addImage(Image image) {
    	images.add(image);
    	image.setProduct(this);
    }
    
    public void removeImage(Image image) {
    	images.remove(image);
    	image.setProduct(null);
    }
    
    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getProducts().add(this);
    }
 
    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getProducts().remove(this);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return productId != null && productId.equals(((Product) o).productId);
    }
 
    @Override
    public int hashCode() {
        return 31;
    }
    
    public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Set<Version> getVersions() {
		return versions;
	}

	public void setVersions(Set<Version> versions) {
		this.versions = versions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public Map<Attribute, AttributeValue> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<Attribute, AttributeValue> attributes) {
		this.attributes = attributes;
	}
	
	
    
}
