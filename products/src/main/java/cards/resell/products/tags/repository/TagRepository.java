package cards.resell.products.tags.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cards.resell.products.tags.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

	public List<Tag> findByName(String name);
}
