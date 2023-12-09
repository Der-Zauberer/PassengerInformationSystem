package eu.derzauberer.pis.structure.model;

public abstract class Entity<T extends Entity<T>> implements Comparable<T> {
	
	public abstract String getId();
	
	@Override
	public int compareTo(T entity) {
		return getId().compareTo(entity.getId());
	}
	
	@Override
	public String toString() {
		final StringBuilder string = new StringBuilder(super.toString());
		string.append("{" + getId());
		if (this instanceof NameEntity) string.append(", " + ((NameEntity) this).getName());
		string.append("}");
		return string.toString();
	}
	
}
