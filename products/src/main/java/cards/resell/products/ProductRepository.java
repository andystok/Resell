package cards.resell.products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	public List<Product> findByTagsName(String tagName);
	
}
