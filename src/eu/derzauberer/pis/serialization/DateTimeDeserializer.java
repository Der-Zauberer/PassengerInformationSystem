package eu.derzauberer.pis.serialization;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateTimeDeserializer extends StdDeserializer<LocalDateTime> {

	private static final long serialVersionUID = 1L;

	public DateTimeDeserializer() {
		this(null);
	}

	public DateTimeDeserializer(Class<LocalDateTime> t) {
		super(t);
	}

	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JacksonException {
		return LocalDateTime.parse(parser.getCodec().readValue(parser, String.class), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

}