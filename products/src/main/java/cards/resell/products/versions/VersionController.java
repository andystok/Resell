package cards.resell.products.versions;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VersionController {

	@Autowired
	VersionRepository versionRepository;
	
	@GetMapping("/version")
	public List<Version> getAllVersions(){
		return versionRepository.findAll();
	}
	
	@PostMapping("/version")
	public Version createVersion(@Valid @RequestBody Version version) {
		return versionRepository.save(version);		
	}
	
	@PutMapping("/version/{versionId}")
	public Version updateVersion(@Valid @RequestBody Version version, @PathVariable(name = "versionId") Long versionId) {
		version.setVersionId(versionId);
		return versionRepository.save(version);	
	}
	
	
}
