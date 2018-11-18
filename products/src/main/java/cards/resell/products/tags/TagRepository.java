package cards.resell.products.tags;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

	public List<Tag> findByName(String name);
}
