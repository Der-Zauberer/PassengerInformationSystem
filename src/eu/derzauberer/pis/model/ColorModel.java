package eu.derzauberer.pis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorModel {
	
	private String textColor;
	private String backgroundColor;
	
	public ColorModel() {
		textColor = "#ffffff";
		backgroundColor = "#808080";
	}
	
	public ColorModel(String textColor, String backgroundColor) {
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
	}
	
	public ColorModel(ColorModel color) {
		this.textColor = color.textColor;
		this.backgroundColor = color.backgroundColor;
	}

}
