package cards.resell.products.attributes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttributeController {
	
	@Autowired
	AttributesRepository attributesRepository;
	
	@Autowired
	AttributeValuesRepository attributeValuesRepository;
	
	@GetMapping("/attribute/name/{name}")
	public Attribute getAttributeByName(@PathVariable(name = "name") String name) {
		// This should always return a list of exactly one Attribute, if more than one is found, return the top one for now
		// I do not plan on allowing tags with duplicate names. 
		List<Attribute> attributes = attributesRepository.findByName(name);
		if (attributes.isEmpty()) {
			return null;
		}		
		return attributes.get(0);
	}
	
	@PostMapping("/attribute")
	public Attribute createAttribute(@RequestBody Attribute attribute) {
		return attributesRepository.save(attribute);
	}

}
