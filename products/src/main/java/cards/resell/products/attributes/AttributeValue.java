package cards.resell.products.attributes;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cards.resell.products.Product;

@Entity
@Table(name = "attribute_values", uniqueConstraints={@UniqueConstraint(columnNames={"name","attribute"})})
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class AttributeValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long attributeValueId;

	private String name;
	
	@ManyToOne(targetEntity=Attribute.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "attribute")
	private Attribute attribute;
	
	@ManyToMany(mappedBy = "attributes" )
	private Set<Product> products = new HashSet<>();
	
	@Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public AttributeValue() {}
    
    public AttributeValue(Attribute attribute, String name) {
    	setName(name);
    	setAttribute(attribute);
    }
    
	public Long getAttributeValueId() {
		return attributeValueId;
	}

	public void setAttributeValueId(Long attributeValueId) {
		this.attributeValueId = attributeValueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
    
    
}
