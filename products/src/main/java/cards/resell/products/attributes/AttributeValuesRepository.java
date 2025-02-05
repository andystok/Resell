package cards.resell.products.attributes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeValuesRepository extends JpaRepository<AttributeValue, Long> {
	
	public List<AttributeValue> findByValueAndAttributeName(String valueName, String attributeName);
}
