package eu.derzauberer.pis.commands;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.util.Command;
import eu.derzauberer.pis.util.FileRepository;

public class PackageCommand extends Command {

	private static final Logger LOGGER = LoggerFactory.getLogger(PackageCommand.class.getSimpleName());
	
	public PackageCommand() {
		super("package");
		setDescription("Puts all existing entities of one type in a single file");
		setUsage("package <type> <file>");
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
			repository.packageEntities(path);
		});
	}

}
