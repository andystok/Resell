package cards.resell.products.versions;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cards.resell.products.Product;
import cards.resell.products.attributes.Attribute;
import cards.resell.products.tags.Tag;

@Entity(name = "Version")
@Table(name = "versions")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Version {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long versionId;
	
	@Column(unique=true)
	private String name;
	
	@ManyToMany(mappedBy = "versions")
    private Set<Product> products = new HashSet<>();

	@ManyToMany(cascade = { 
        CascadeType.PERSIST, 
        CascadeType.MERGE
    })
    @JoinTable(name = "version_tags",
        joinColumns = @JoinColumn(name = "version_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> versionTags = new HashSet<>();
	@ManyToMany(cascade = { 
        CascadeType.PERSIST, 
        CascadeType.MERGE
    })
    @JoinTable(name = "version_attributes",
        joinColumns = @JoinColumn(name = "version_id"),
        inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private Set<Attribute> requiredAttributes = new HashSet<>();
	

	@Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
    
    public Version() {}
    
    public Version(String name) {
    	this.name = name;
    }
    
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return Objects.equals(name, version.name);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Tag> getVersionTags() {
		return versionTags;
	}

	public void setVersionTags(Set<Tag> versionTags) {
		this.versionTags = versionTags;
	}
	
	public Set<Attribute> getRequiredAttributes() {
		return requiredAttributes;
	}

	public void setRequiredAttributes(Set<Attribute> requiredAttributes) {
		this.requiredAttributes = requiredAttributes;
	}
	
}
