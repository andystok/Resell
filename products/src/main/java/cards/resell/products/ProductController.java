package cards.resell.products;

import java.util.List;

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

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@Autowired
	ProductRepository productRepository;

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
		productService.verifyProduct(product);
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
