package cards.resell.products;

import java.util.List;
import java.util.Set;

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
	
	// Get all Products 
	@GetMapping("/products")
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	@GetMapping("/products/tagName/{name}")
	public List<Product> getAllProductsByTagName(@PathVariable(name = "name") String name){
		return productRepository.findByTagName(name);
	}
	
	// Create Product
	// Call one without creating versions or tags, and if fails call again with flags set
	@PostMapping("/products")
	public Product createProduct(@Valid @RequestBody Product product ) {

		// Optional : Create Versions
		if (product.isCreateVersions()) {
			VersionController versionMaker = new VersionController();  
			for (Version version : product.getVersions()) {
				versionMaker.createVersion(version);
			}
		}

		// Optional : Create Tags
		if (product.isCreateTags()) {
			TagsController tagMaker = new TagsController();
			Set<Tag> tags = product.getTags();
			for (Tag tag: tags) {
				tagMaker.createTag(tag);
			}
			tagMaker.createTag(product.getPrimaryCategory());
		}
		
		// Make sure primaries are part of their sets 
		product.getTags().add(product.getPrimaryCategory());		
		product.getVersions().add(product.getPrimaryVersion());
		
		
		// TODO Look at this error handling. Possibly make the caller handle this. 
		Product result = null;
		try { 
			result = productRepository.save(product);
		} catch (Exception e) {
			if (!product.isCreateVersions() || !product.isCreateTags()) {
				product.setCreateTags(true);
				product.setCreateVersions(true);
				result = createProduct(product);
			}
		}
		
		return result; 
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

		product.setProductName(productInformation.getProductName());

	    Product updatedNote = productRepository.save(product);
	    return updatedNote;
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
