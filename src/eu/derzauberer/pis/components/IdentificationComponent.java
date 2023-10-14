package eu.derzauberer.pis.components;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.IdentificationIndex;
import eu.derzauberer.pis.model.NameEntity;
import eu.derzauberer.pis.service.EntityService;

public class IdentificationComponent<T extends Entity<T> & NameEntity> extends Component<EntityService<T>, IdentificationIndex> {
	
	private Function<T, String> identification;
	private IdentificationIndex index;
	private boolean generateIndex = false;
	
	public IdentificationComponent(EntityService<T> service, Function<T, String> identification) {
		super("identification", service, LoggerFactory.getLogger(SearchComponent.class));
		this.identification = identification;
		index = loadAsOptional(IdentificationIndex.class).orElseGet(() -> {
			generateIndex = true;
			return new IdentificationIndex(new HashMap<>());
		});
		if (generateIndex) {
			getService().getList().forEach(this::add);
			save(index);
		}
		getService().addOnAdd(entity -> {
			add(entity);
			save(index);
		});
		getService().addOnRemove(id -> {
			index.getEntries().remove(id);
			save(index);
		});
	}
	
	private void add(T entity) {
		final String secondaryId = identification.apply(entity);
		if (secondaryId == null) return;
		final String id = index.getEntries().get(secondaryId);
		if (id != null && (!id.equals(entity.getId()) || getService().getById(secondaryId).isPresent())) {
			throw new IllegalArgumentException("Identification " + secondaryId + " already exists!");
		}
		index.getEntries().put(identification.apply(entity), entity.getId());
	}
	
	public Optional<T> get(String id) {
		return getService().getById(index.getEntries().get(id));
	}

}
