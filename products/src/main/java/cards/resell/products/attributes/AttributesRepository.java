package cards.resell.products.attributes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributesRepository extends JpaRepository<Attribute, Long> {

	public List<Attribute> findByName(String name);
}
