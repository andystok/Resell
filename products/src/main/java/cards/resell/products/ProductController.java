package cards.resell.products;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cards.resell.products.attributes.Attribute;
import cards.resell.products.attributes.AttributeController;
import cards.resell.products.attributes.AttributeValue;
import cards.resell.products.attributes.AttributeValuesController;
import cards.resell.products.exception.ResourceNotFoundException;
import cards.resell.products.repository.ProductRepository;
import cards.resell.products.tags.Tag;
import cards.resell.products.tags.TagsController;
import cards.resell.products.versions.Version;
import cards.resell.products.versions.VersionController;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired 
	TagsController tagger;
	
	@Autowired
	VersionController versioner;
	
	@Autowired 
	AttributeValuesController valuer;
	
	@Autowired
	AttributeController attributor;
	
	
	// Get all Products 
	@GetMapping("/products")
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	@GetMapping("/products/tagName/{name}")
	public List<Product> getAllProductsByTagName(@PathVariable(name = "name") String name){
		return productRepository.findByTagsName(name);
	}
	
	@PostMapping("/products")
	public Product createProduct(@Valid @RequestBody Product product ) {	
		for(Tag tag : product.getTags()) {
			Tag existingTag = tagger.getTagByName(tag.getName()); 
			if (existingTag == null) {
				tag = tagger.createTag(tag);
			} else {
				tag.setTagId(existingTag.getTagId());
			}
		}
		for(Version version : product.getVersions()) {
			Version existingVersion = versioner.getVersionByName(version.getName()); 
			if (existingVersion == null) {
				version = versioner.createVersion(version);
			} else {
				version.setVersionId(existingVersion.getVersionId());
			}
		}
		
		Map<Attribute, AttributeValue> attributes = product.getAttributes();
		Map<Attribute, AttributeValue> newAttributes = new HashMap<>();

		// Create a Iterator to EntrySet of HashMap
		Iterator<Entry<Attribute, AttributeValue>> entryIt = attributes.entrySet().iterator();
		 
		// Iterate over all the elements
		while (entryIt.hasNext()) {
			Entry<Attribute, AttributeValue> attr = entryIt.next();
			Attribute existingAttribute = attributor.getAttributeByName(attr.getKey().getName()); 
			if (existingAttribute == null) {
				existingAttribute = attributor.createAttribute(attr.getKey());				
			}
			entryIt.remove();
			newAttributes.put(existingAttribute, attr.getValue());
		}
		attributes.putAll(newAttributes);
		
		for(Entry<Attribute, AttributeValue> attr : attributes.entrySet()) {
			AttributeValue existingValue = valuer.getAttributeValueByName(attr.getValue().getValue(), attr.getKey().getName());
			if (existingValue == null) {
				AttributeValue newAttribute = new AttributeValue(attr.getKey(), attr.getValue().getValue());
				attr.setValue(valuer.createAttributeValue(newAttribute));				
			} else {
				attr.getValue().setAttributeValueId(existingValue.getAttributeValueId());
			}
		}
		return productRepository.save(product);
	}
	
	// Get a single Product
	@GetMapping("/products/{id}")
	public Product getProductById(@PathVariable(value ="id") Long productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
	}
	
	// Update a product 
	@PutMapping("/products/{id}")
	public Product updateProduct(@PathVariable(value = "id") Long productId, 
			@Valid @RequestBody Product productInformation) {
		Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
		
		productInformation.setProductId(product.getProductId());
		
 	    return productRepository.save(productInformation);
	}
	
	// Delete a Product 
	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
		productRepository.delete(product);
		
		return ResponseEntity.ok().build();
	}
	
	
}
