package cards.resell.products.attributes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttributeValuesController {
	
	@Autowired
	AttributeValuesRepository attributeValuesRepository;
	
	@GetMapping("/attribute/{attributeName}/value/{name}")
	public AttributeValue getAttributeValueByName(@PathVariable(name = "name") String name, 
			@PathVariable(name = "attributeName") String attributeName ) {
		// This should always return a list of exactly one Value per Attribute Name, 
		// if more than one is found, return the top one for now
		// I do not plan on allowing values with duplicate names per Attribute. 
		List<AttributeValue> values = attributeValuesRepository.findByNameAndAttributeName(name, attributeName);
		if(values.isEmpty()) {
			return null;
		}		
		return values.get(0);
	}
	
	@PostMapping("/attribute/{attributeName}/value")
	public AttributeValue createAttributeValue(@RequestBody AttributeValue attributeValue) {
		return attributeValuesRepository.save(attributeValue);
	}

}
