package eu.derzauberer.pis.converter;

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
		platformForm.setLinkedPlatforms(platform.getLinkedPlatforms().stream().toList());
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
		platformForm.getLinkedPlatforms().forEach(platform.getLinkedPlatforms()::add);
		return platform;
	}

}
