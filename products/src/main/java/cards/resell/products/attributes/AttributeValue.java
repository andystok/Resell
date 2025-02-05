package cards.resell.products.attributes;

import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "attribute_values", uniqueConstraints={@UniqueConstraint(columnNames={"value","attribute"})})
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class AttributeValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long attributeValueId;

	private String value;
	
	@ManyToOne(targetEntity=Attribute.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "attribute")
	private Attribute attribute;
	
	@Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public AttributeValue() {}
    
    public AttributeValue(String value) {
    	setValue(value);
    }
    
    public AttributeValue(Attribute attribute, String value) {
    	setValue(value);
    	setAttribute(attribute);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeValue attribute = (AttributeValue) o;
        return Objects.equals(value, attribute.getValue());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
	public Long getAttributeValueId() {
		return attributeValueId;
	}

	public void setAttributeValueId(Long attributeValueId) {
		this.attributeValueId = attributeValueId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
}
