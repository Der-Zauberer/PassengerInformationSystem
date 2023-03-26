package eu.derzauberer.pis.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.util.Command;
import eu.derzauberer.pis.util.Repository;

public class ExtractCommand extends Command {

	public ExtractCommand() {
		super("extract", "pis extract");
		setDescription("Adds all entities from a single file");
		addBranch("<type> <file>", "Adds all entities from a single file");
		addBranch("-l", "List all availible <type>s");
		setMinArguments(2);
		setAction(args -> {
			if (Arrays.asList(args).contains("-l")) {
				printList(Pis.getRepositories());
				return;
			}
			final Repository<?> service = Pis.getRepository(args[0]);
			Path path;
			if (service == null) {
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
			service.extractEntities(path);
		});
	}

}
