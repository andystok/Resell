package cards.resell.products.images;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>{

	public List<Image> findByPath(String path);
}
