package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

import eu.derzauberer.pis.enums.InformationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformationModel implements Comparable<InformationModel> {
	
	private String title;
	private String text;
	private InformationType type;
	
	@ConstructorProperties({ "title", "text", "type" })
	public InformationModel(String title, String text, InformationType type) {
		super();
		this.title = title;
		this.text = text;
		this.type = type;
	}
	
	public InformationModel(InformationModel information) {
		this.title = information.title;
		this.text = information.text;
		this.type = information.type;
	}

	@Override
	public int compareTo(InformationModel information) {
		return Integer.compare(information.getType().getPriority(), getType().getPriority());
	}

}
