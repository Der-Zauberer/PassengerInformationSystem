package eu.derzauberer.pis.persistence;

public abstract class Entity<T extends Entity<T>> implements Identifiable, Comparable<T> {
	
	public abstract T copy();
	
	@Override
	public int compareTo(T entity) {
		return getId().compareTo(entity.getId());
	}
	
	@Override
	public String toString() {
		final StringBuilder string = new StringBuilder(super.toString());
		string.append("{" + getId());
		if (this instanceof Namable) string.append(", " + ((Namable) this).getName());
		string.append("}");
		return string.toString();
	}
	
}
