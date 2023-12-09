package eu.derzauberer.pis.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.service.EntityService;
import eu.derzauberer.pis.structure.model.Entity;
import eu.derzauberer.pis.structure.model.NameEntity;

public class IdentificationComponent<T extends Entity<T> & NameEntity> extends Component<EntityService<T>, IdentificationComponent.Index> {
	
	private Index index;
	private boolean generateIndex = false;
	
	public IdentificationComponent(EntityService<T> service, Function<T, String> attribute) {
		super("identification", service, LoggerFactory.getLogger(SearchComponent.class));
		index = loadAsOptional(Index.class).orElseGet(() -> {
			generateIndex = true;
			return new Index(new HashMap<>());
		});
		if (generateIndex) {
			getService().getAll().forEach(entity -> {
				final String identification = attribute.apply(entity);
				if (identification != null && !identification.isEmpty()) {
					index.entries().put(identification, entity.getId());
				}
			});
			save(index);
		}
		getService().addOnSave(event -> {
			final String newIdentification = attribute.apply(event.newEntity());
			final boolean newIndexRequired = event.oldEntity().map(attribute::apply).filter(identification -> identification.equals(newIdentification)).isEmpty();
			if (newIndexRequired) {
				event.oldEntity().ifPresent(entity -> index.entries().remove(attribute.apply(entity)));
				if (newIdentification != null && !newIdentification.isEmpty()) {
					index.entries().put(newIdentification, event.newEntity().getId());
				}
				save(index);
			}
		});
		getService().addOnRemove(event -> {
			index.entries().remove(attribute.apply(event.oldEntity()));
			save(index);
		});
	}
	
	public Optional<T> getByIdentification(String identification) {
		final String id = index.entries().get(identification);
		if (id == null) return Optional.empty();
		return getService().getById(id);
	}
	
	public record Index(Map<String, String> entries) {}

}
