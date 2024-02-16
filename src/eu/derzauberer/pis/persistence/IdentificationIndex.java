package eu.derzauberer.pis.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.service.EntityService;

public class IdentificationIndex<T extends Entity<T> & Namable> {
	
	private final EntityService<T> service;
	private final JsonFileHandler<IdentificationIndexContent> fileHandler;
	private final IdentificationIndexContent index;
	private boolean generateIndex = false;
	
	private static final String FILE_NAME = "ids";
	
	public IdentificationIndex(EntityService<T> service, Function<T, String> attribute) {
		final Logger logger = LoggerFactory.getLogger(IdentificationIndex.class);
		fileHandler = new JsonFileHandler<>("id index", service.getName(), IdentificationIndexContent.class, logger);
		this.service = service;
		
		index = fileHandler.loadAsOptional(FILE_NAME).orElseGet(() -> {
			generateIndex = true;
			return new IdentificationIndexContent(new HashMap<>());
		});
		
		if (generateIndex) {
			service.getAll().stream().map(Lazy::get).forEach(entity -> {
				final String identification = attribute.apply(entity);
				if (identification != null && !identification.isEmpty()) {
					index.entries().put(identification, entity.getId());
				}
			});
			fileHandler.save(FILE_NAME, index);
		}
		
		service.addOnSave(event -> {
			final String newIdentification = attribute.apply(event.newEntity());
			final boolean newIndexRequired = event.oldEntity().map(attribute::apply).filter(identification -> identification.equals(newIdentification)).isEmpty();
			if (newIndexRequired) {
				event.oldEntity().ifPresent(entity -> index.entries().remove(attribute.apply(entity)));
				if (newIdentification != null && !newIdentification.isEmpty()) {
					index.entries().put(newIdentification, event.newEntity().getId());
				}
				fileHandler.save(FILE_NAME, index);
			}
		});
		
		service.addOnRemove(event -> {
			index.entries().remove(attribute.apply(event.oldEntity()));
			fileHandler.save(FILE_NAME, index);
		});
	}
	
	public Optional<T> getByIdentification(String identification) {
		final String id = index.entries().get(identification);
		if (id == null) return Optional.empty();
		return service.getById(id);
	}
	
	public record IdentificationIndexContent(Map<String, String> entries) {}

}
