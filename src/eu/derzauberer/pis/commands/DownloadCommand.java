package eu.derzauberer.pis.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import eu.derzauberer.pis.downloader.DbRisPlatformsDownloader;
import eu.derzauberer.pis.downloader.DbRisStationsDownloader;
import eu.derzauberer.pis.downloader.DbStadaStationDownloader;
import eu.derzauberer.pis.util.Command;

public class DownloadCommand extends Command {

	public DownloadCommand() {
		super("download", "pis download");
		setDescription("Downloads entities from an api to the data directory");
		addBranch("<downloader>", "Downloads entities from an api to the data directory. Download an existing entity will only\nupdate the downloaded fields instead of overwriting the whole entity.");
		addBranch("-l", "List all availible <downloader>");
		setMinArguments(1);
		setAction(args -> {
			final Map<String, Consumer<String[]>> downloaderMap = new HashMap<>();
			downloaderMap.put(DbStadaStationDownloader.getName(), (donwloaderArgs) -> new DbStadaStationDownloader());
			downloaderMap.put(DbRisStationsDownloader.getName(), (donwloaderArgs) -> new DbRisStationsDownloader());
			downloaderMap.put(DbRisPlatformsDownloader.getName(), (donwloaderArgs) -> new DbRisPlatformsDownloader());
			if (Arrays.asList(args).contains("-l")) {
				printList(downloaderMap.keySet());
				return;
			}
			final Consumer<String[]> downloader = downloaderMap.get(args[0]);
			if (downloader == null) {
				LOGGER.error("The downloader {} does not exist!", args[0]);
				return;
			}
			final String[] downloaderArgs = args.length == 0 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);
			downloader.accept(downloaderArgs);
		});
	}

}
