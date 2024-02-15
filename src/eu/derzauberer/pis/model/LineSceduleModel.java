package eu.derzauberer.pis.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineSceduleModel {
	
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;
	
	private Boolean holiday;
	private Boolean scoolHoliday;
	
	private final List<LocalDate> inclusiveDates = new ArrayList<>();
	private final List<LocalDate> exclusiveDates = new ArrayList<>();
	
	public LineSceduleModel() {}
	
	public LineSceduleModel(LineSceduleModel lineScedule) {
		this.monday = lineScedule.monday;
		this.tuesday = lineScedule.tuesday;
		this.wednesday = lineScedule.wednesday;
		this.thursday = lineScedule.thursday;
		this.friday = lineScedule.friday;
		this.saturday = lineScedule.saturday;
		this.sunday = lineScedule.sunday;
		this.holiday = lineScedule.holiday;
		this.scoolHoliday = lineScedule.scoolHoliday;
		this.inclusiveDates.addAll(lineScedule.inclusiveDates);
		this.inclusiveDates.addAll(lineScedule.exclusiveDates);
	}

}
