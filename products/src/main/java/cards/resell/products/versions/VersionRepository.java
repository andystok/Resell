package cards.resell.products.versions;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<Version, Long>{

	public List<Version> findByName(String name);
}
