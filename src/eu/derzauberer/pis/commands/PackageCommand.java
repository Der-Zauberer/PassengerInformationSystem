package eu.derzauberer.pis.commands;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.repositories.Repository;
import eu.derzauberer.pis.util.Command;

public class PackageCommand extends Command {
	
	public PackageCommand() {
		super("package", "pis package");
		setDescription("Puts all existing entities of one type in a single file");
		addBranch("<type> <file>", "Puts all existing entities of one type in a single file");
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
			service.packageEntities(path);
		});
	}

}
