package eu.derzauberer.pis.structure.model;

public abstract class EntityModel<T extends EntityModel<T>> implements Comparable<T> {
	
	public abstract String getId();
	
	public abstract T copy();
	
	@Override
	public int compareTo(T entity) {
		return getId().compareTo(entity.getId());
	}
	
	@Override
	public String toString() {
		final StringBuilder string = new StringBuilder(super.toString());
		string.append("{" + getId());
		if (this instanceof NameEntityModel) string.append(", " + ((NameEntityModel) this).getName());
		string.append("}");
		return string.toString();
	}
	
}
