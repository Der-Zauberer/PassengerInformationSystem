package eu.derzauberer.pis.serialization;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class TimeDeserializer extends StdDeserializer<LocalTime> {

	private static final long serialVersionUID = 1L;

	public TimeDeserializer() {
		this(null);
	}

	public TimeDeserializer(Class<LocalTime> t) {
		super(t);
	}

	@Override
	public LocalTime deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JacksonException {
		return LocalTime.parse(parser.getCodec().readValue(parser, String.class), DateTimeFormatter.ofPattern("HH:mm"));
	}

}