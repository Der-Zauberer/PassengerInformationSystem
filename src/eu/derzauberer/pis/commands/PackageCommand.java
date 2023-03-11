package eu.derzauberer.pis.commands;

import java.nio.file.Path;
import java.nio.file.Paths;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.util.Command;
import eu.derzauberer.pis.util.FileRepository;

public class PackageCommand extends Command {

	public PackageCommand() {
		super("package");
		setMinArguments(2);
		setAction(args -> {
			final FileRepository<?, ?> repository = Pis.getRepositories().get(args[0]);
			Path path;
			if (repository == null) {
				consoleOut("Error: The repository " + args[0] + " does not exist!");
				return;
			}
			try {
				path = Paths.get(args[1]);
			} catch (Exception exception) {
				consoleOut("Error: The path " + args[0] + " is not valid!");
				return;
			}
			repository.packageEntities(path);
			consoleOut("Success: Packaged " + args[0] + " to " + args[1] + "!");
		});
	}

}
