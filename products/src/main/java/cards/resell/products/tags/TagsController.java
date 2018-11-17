package cards.resell.products.tags;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cards.resell.products.tags.repository.TagRepository;

@RestController
@RequestMapping("/api")
public class TagsController {
	
	@Autowired
	TagRepository tagRepository;
	
	@GetMapping("/tags")
	public List<Tag> getAllTags() {
		return tagRepository.findAll();
	}
	
	@GetMapping("/tags/{id}")
	public Tag getTagById(@PathVariable(name = "id") Long id) {
		return tagRepository.getOne(id);
	}
	
	@GetMapping("/tags/name/{name}")
	public Tag getTagByName(@PathVariable(name = "name") String name) {
		// This should always return a list of exactly one Tag, if more than one is found, return the top one for now
		// I do not plan on allowing tags with duplicate names.
		List<Tag> tagList = tagRepository.findByName(name);
		if (tagList.isEmpty()) {
			return null;
		}
		return tagRepository.findByName(name).get(0);
	}
	
	@PostMapping("/tags")
	public Tag createTag(@Valid @RequestBody Tag tag) {
		return tagRepository.save(tag);
	}
}
