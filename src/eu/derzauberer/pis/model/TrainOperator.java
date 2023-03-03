package eu.derzauberer.pis.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonPropertyOrder({"id", "name", "adress"})
public class TrainOperator extends Entity<String> {

	private final Adress adress = new Adress();
	
	public TrainOperator(String id, String name) {
		super(id, name);
	}
	
	public Adress getAdress() {
		return adress;
	}

}
