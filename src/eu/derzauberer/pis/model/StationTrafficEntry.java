package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalTime;

public class StationTrafficEntry implements Comparable<StationTrafficEntry> {
	
	private final LocalTime time;
	private final String lineId;
	private final int stationPosition;
	private final boolean lastStation;
	
	@ConstructorProperties({ "time", "lineId", "stationPosition", "platform", "platfromArea", "lastStation" })
	public StationTrafficEntry(LocalTime time, String lineId, int stationPosition, int platform, String platfromArea, boolean lastStation) {
		super();
		this.time = time;
		this.lineId = lineId;
		this.stationPosition = stationPosition;
		this.lastStation = lastStation;
	}
	
	public LocalTime getTime() {
		return time;
	}
	
	public String getLineId() {
		return lineId;
	}
	
	public int getStationPosition() {
		return stationPosition;
	}
	
	public boolean isLastStation() {
		return lastStation;
	}
	
	@Override
	public int compareTo(StationTrafficEntry entry) {
		return time.compareTo(entry.getTime());
	}

}
