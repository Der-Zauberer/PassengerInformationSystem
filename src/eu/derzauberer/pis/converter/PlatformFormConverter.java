package eu.derzauberer.pis.converter;

import java.util.Arrays;
import java.util.Iterator;

import org.springframework.stereotype.Component;

import eu.derzauberer.pis.structure.container.Platform;
import eu.derzauberer.pis.structure.form.PlatformForm;

@Component
public class PlatformFormConverter implements FormConverter<Platform, PlatformForm> {

	@Override
	public PlatformForm convertToForm(Platform platform) {
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
	public Platform convertToModel(PlatformForm platformForm) {
		final Platform platform = new Platform(platformForm.getName());
		return convertToModel(platform, platformForm);
	}

	@Override
	public Platform convertToModel(Platform platform, PlatformForm platformForm) {
		platform.setLength(platformForm.getLength());
		if (platformForm.getLinkedPlatforms() != null) {
			final String platforms[] = platformForm.getLinkedPlatforms().split(",");
			Arrays.stream(platforms).map(link -> link.trim()).forEach(platform.getLinkedPlatforms()::add);
		}
		return platform;
	}

}
