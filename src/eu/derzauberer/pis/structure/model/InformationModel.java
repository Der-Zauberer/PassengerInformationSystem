package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;

import eu.derzauberer.pis.structure.enums.InformationType;

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

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public InformationType getType() {
		return type;
	}
	
	public void setType(InformationType type) {
		this.type = type;
	}

	@Override
	public int compareTo(InformationModel o) {
		return Integer.compare(o.getType().getPriority(), getType().getPriority());
	}

}
