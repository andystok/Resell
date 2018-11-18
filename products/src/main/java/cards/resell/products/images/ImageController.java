package cards.resell.products.images;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ImageController {
	
	@Autowired
	ImageRepository imageRepository;
	
	@GetMapping("/image")
	public List<Image> getAllImages(){
		return imageRepository.findAll();
	}
	
	@PostMapping("/image")
	public Image createVersion(@Valid @RequestBody Image image) {
		return imageRepository.save(image);		
	}
	
	@GetMapping("/image/path/{path}")
	public Image getVersionByName(@PathVariable(name = "path") String path) {
		List<Image> images = imageRepository.findByPath(path);
		if (images.isEmpty()) {
			return null;
		}
		return images.get(0);
	}	

}
