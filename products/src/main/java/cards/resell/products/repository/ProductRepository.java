package cards.resell.products.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cards.resell.products.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	public List<Product> findByTagName(String tagName);
	
	public List<Product> findByPrimaryCategoryName(String name);
}
