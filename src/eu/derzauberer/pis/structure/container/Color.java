package eu.derzauberer.pis.structure.container;


public class Color {
	
	private int textColor ;
	private int backgroundColor;
	
	
	public Color() {
		textColor = 0xffffff;
		backgroundColor = 0;
	}
	
	public Color(int textColor, int backgroundColor) {
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
	}
	
	public Color(Color color) {
		this.textColor = color.textColor;
		this.backgroundColor = color.backgroundColor;
	}
	
	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	
	public int getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

}
