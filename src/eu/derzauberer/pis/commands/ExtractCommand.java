package eu.derzauberer.pis.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.util.Command;
import eu.derzauberer.pis.util.FileRepository;

public class ExtractCommand extends Command {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractCommand.class.getSimpleName());

	public ExtractCommand() {
		super("extract");
		setDescription("Adds all entities from a single file");
		setUsage("extract <type> <file>");
		setMinArguments(2);
		setAction(args -> {
			final FileRepository<?, ?> repository = Pis.getRepository(args[0]);
			Path path;
			if (repository == null) {
				LOGGER.error("The repository {} does not exist!", args[0]);
				return;
			}
			try {
				path = Paths.get(args[1]);
			} catch (Exception exception) {
				LOGGER.error("The path {} is not valid!", args[1]);
				return;
			}
			if (!Files.exists(path)) {
				LOGGER.error("The file {} does not exist!", args[1]);
				return;
			}
			repository.extractEntities(path);
		});
	}

}
