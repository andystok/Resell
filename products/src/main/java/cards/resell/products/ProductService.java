package cards.resell.products;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cards.resell.products.attributes.Attribute;
import cards.resell.products.attributes.AttributeController;
import cards.resell.products.attributes.AttributeValue;
import cards.resell.products.attributes.AttributeValuesController;
import cards.resell.products.images.Image;
import cards.resell.products.images.ImageController;
import cards.resell.products.tags.Tag;
import cards.resell.products.tags.TagsController;
import cards.resell.products.versions.Version;
import cards.resell.products.versions.VersionController;

@Service
public class ProductService {

	@Autowired 
	TagsController tagger;
	
	@Autowired
	VersionController versioner;
	
	@Autowired 
	AttributeValuesController valuer;
	
	@Autowired
	AttributeController attributor;
	
	@Autowired
	ImageController imgur;
	
	public void verifyProduct(Product product) {
		
		Set<Tag> tags = product.getTags();
		if (!tags.isEmpty()) {
			verifyTags(tags);
		}
		
		Set<Version> versions = product.getVersions();
		if (!versions.isEmpty()) {
			verifyVersions(versions);
		}

		Map<Attribute, AttributeValue> attributes = product.getAttributes();
		if (!attributes.isEmpty()) {
			verifyAttributes(attributes);
		}
		
		createImages(product);
		
	}
	
	private void createImages(Product product) {
		Set<Image> newImages = new HashSet<>();
		for(Image image : product.getImages()) {
			newImages.add(imgur.createVersion(image));
		}
		product.setImages(newImages);
	}
	
	private void verifyTags(Set<Tag> tags) {
		for(Tag tag : tags) {
			Tag existingTag = tagger.getTagByName(tag.getName()); 
			if (existingTag == null) {
				tag = tagger.createTag(tag);
			} else {
				tag.setTagId(existingTag.getTagId());
			}
		}
	}
	
	private void verifyVersions(Set<Version> versions) {
		for(Version version : versions) {
			Version existingVersion = versioner.getVersionByName(version.getName()); 
			if (existingVersion == null) {
				version = versioner.createVersion(version);
			} else {
				version.setVersionId(existingVersion.getVersionId());
			}
		}
	}
	
	private void verifyAttributes (Map<Attribute, AttributeValue> attributes) {
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
		
	}
	
	
	
	
}
