package eu.derzauberer.pis.serialization;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateDeserializer extends StdDeserializer<LocalDate> {

	private static final long serialVersionUID = 1L;

	public DateDeserializer() {
		this(null);
	}

	public DateDeserializer(Class<LocalDate> t) {
		super(t);
	}

	@Override
	public LocalDate deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JacksonException {
		return LocalDate.parse(parser.getCodec().readValue(parser, String.class), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

}