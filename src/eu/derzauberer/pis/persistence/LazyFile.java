package eu.derzauberer.pis.persistence;

import java.nio.file.Path;
import java.util.function.Function;

public class LazyFile<T> {
	
	private Path path;
	private Function<Path, T> loadFunction;
	
	public LazyFile(Path path, Function<Path, T> loadFunction) {
		this.path = path;
		this.loadFunction = loadFunction;
	}
	
	public Path getPath() {
		return path;
	}
	
	public T load() {
		return loadFunction.apply(path);
	}

}
