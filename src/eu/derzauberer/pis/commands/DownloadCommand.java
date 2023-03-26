package eu.derzauberer.pis.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import eu.derzauberer.pis.downloader.DbRisPlatformsDownloader;
import eu.derzauberer.pis.downloader.DbRisStationsDownloader;
import eu.derzauberer.pis.downloader.DbStationDownloader;
import eu.derzauberer.pis.util.Command;
import eu.derzauberer.pis.util.Downloader;

public class DownloadCommand extends Command {

	public DownloadCommand() {
		super("download", "pis download");
		setDescription("Downloads entities from an api to the data directory");
		addBranch("<downloader>", "Downloads entities from an api to the data directory. Download an existing entity will only\nupdate the downloaded fields instead of overwriting the whole entity.");
		addBranch("-l", "List all availible <downloader>");
		setMinArguments(1);
		setAction(args -> {
			final Map<String, Downloader> downloaderMap = new HashMap<>();
			downloaderMap.put(DbStationDownloader.getName(), new DbStationDownloader());
			downloaderMap.put(DbRisStationsDownloader.getName(), new DbRisStationsDownloader());
			downloaderMap.put(DbRisPlatformsDownloader.getName(), new DbRisPlatformsDownloader());
			if (Arrays.asList(args).contains("-l")) {
				printList(downloaderMap.keySet());
				return;
			}
			final Downloader downloader = downloaderMap.get(args[0]);
			if (downloader == null) {
				LOGGER.error("The downloader {} does not exist!", args[0]);
				return;
			}
			downloader.download();
		});
	}

}
