package eu.derzauberer.pis.converter;

import java.util.Arrays;
import java.util.Iterator;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.dto.PlatformForm;
import eu.derzauberer.pis.structure.model.PlatformModel;

@Component
public class PlatformFormConverter implements FormConverter<PlatformModel, PlatformForm> {

	@Override
	public PlatformForm convertToForm(PlatformModel platform) {
		final PlatformForm platformForm = new PlatformForm();
		platformForm.setName(platform.getName());
		platformForm.setLength(platform.getLength());
		final Iterator<String> platforms = platform.getLinkedPlatforms().iterator();
		final StringBuilder string = new StringBuilder();
		while(platforms.hasNext()) {
			string.append(platforms.next());
			if (platforms.hasNext()) string.append(", ");
		}
		platformForm.setLinkedPlatforms(string.toString());
		return platformForm;
	}

	@Override
	public PlatformModel convertToModel(PlatformForm platformForm) {
		final PlatformModel platform = new PlatformModel(platformForm.getName());
		return convertToModel(platform, platformForm);
	}

	@Override
	public PlatformModel convertToModel(PlatformModel platform, PlatformForm platformForm) {
		platform.setLength(platformForm.getLength());
		if (platformForm.getLinkedPlatforms() != null) {
			final String platforms[] = platformForm.getLinkedPlatforms().split(",");
			Arrays.stream(platforms).map(link -> link.trim()).forEach(platform.getLinkedPlatforms()::add);
		}
		return platform;
	}

}
