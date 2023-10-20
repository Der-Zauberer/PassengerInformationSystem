package eu.derzauberer.pis.components;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.index.IdentificationIndex;
import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.NameEntity;
import eu.derzauberer.pis.service.EntityService;

public class IdentificationComponent<T extends Entity<T> & NameEntity> extends Component<EntityService<T>, IdentificationIndex> {
	
	private IdentificationIndex index;
	private boolean generateIndex = false;
	
	public IdentificationComponent(EntityService<T> service, Function<T, String> identification) {
		super("identification", service, LoggerFactory.getLogger(SearchComponent.class));
		index = loadAsOptional(IdentificationIndex.class).orElseGet(() -> {
			generateIndex = true;
			return new IdentificationIndex(new HashMap<>());
		});
		if (generateIndex) {
			getService().getList().forEach(entity -> index.getEntries().put(identification.apply(entity), entity.getId()));
			save(index);
		}
		getService().addOnAdd(entity -> {
			index.getEntries().put(identification.apply(entity), entity.getId());
			save(index);
		});
		getService().addOnRemove(id -> {
			index.getEntries().remove(id);
			save(index);
		});
	}
	
	public Optional<T> get(String identification) {
		final String id = index.getEntries().get(identification);
		if (id == null) return Optional.empty();
		return getService().getById(id);
	}

}
