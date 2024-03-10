package eu.derzauberer.pis.dto;

import java.util.Arrays;
import java.util.Iterator;

import eu.derzauberer.pis.model.PlatformModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlatformForm {
	
	@NotBlank
	private String name;
	
	@PositiveOrZero
	private int length;
	
	private String linkedPlatforms;
	
	public PlatformForm(PlatformModel platform) {
		name = platform.getName();
		length = platform.getLength();
		final Iterator<String> linkedIterator = platform.getLinkedPlatforms().iterator();
		final StringBuilder linkedString = new StringBuilder();
		while(linkedIterator.hasNext()) {
			linkedString.append(linkedIterator.next());
			if (linkedIterator.hasNext()) linkedString.append(", ");
		}
		linkedPlatforms = linkedString.toString();
	}
	
	public PlatformModel toPlatformModel() {
		return toPlatformModel(new PlatformModel(name));
	}
	
	public PlatformModel toPlatformModel(PlatformModel existingPlatform) {
		existingPlatform.setLength(length);
		if (linkedPlatforms != null && !linkedPlatforms.isBlank()) {
			final String platforms[] = linkedPlatforms.split(",");
			Arrays.stream(platforms).map(link -> link.trim()).forEach(existingPlatform.getLinkedPlatforms()::add);
		}
		return existingPlatform;
	}

}
