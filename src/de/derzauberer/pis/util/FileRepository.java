package de.derzauberer.pis.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import de.derzauberer.pis.model.Entity;

public class FileRepository<T extends Entity<I>, I> {
	
	private final String DIRECTORY = "data";
	private final String name;
	private final String fileType;
	private final Set<I> entities = new HashSet<>();
	
	public FileRepository(String name, String fileType) {
		this.name = name;
		this.fileType = fileType;
		try {
			Files.createDirectories(Paths.get(DIRECTORY, name));
			Files.list(Paths.get(DIRECTORY, name)).forEach(path -> {
				try {
					final String content = Files.readString(path); //TODO
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			});
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public void add(T entity) {
		entities.add(entity.getId());
		try {
			final Path path = Paths.get(DIRECTORY, name, entity.getId().toString() + "." + fileType);
			String content = null; //TODO
			Files.writeString(path, content, StandardOpenOption.CREATE);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public void remove(T entity) {
		remove(entity.getId());
	}
	
	public void remove(I id) {
		entities.remove(id);
		try {
			Files.deleteIfExists(Paths.get(DIRECTORY, name, id.toString() + "." + fileType));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public Optional<T> get(I id) {
		final Path path = Paths.get(DIRECTORY, name, id.toString() + "." + fileType);
		if (entities.contains(id) && Files.exists(path)) {
			try {
				String content = Files.readString(path);
				return Optional.of(null); //TODO
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		return Optional.empty();
	}
	
	public boolean contains(I id) {
		return entities.contains(id);
	}

}
