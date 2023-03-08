package eu.derzauberer.pis.serialization;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DateTimeSerializer extends StdSerializer<LocalDateTime> {

	private static final long serialVersionUID = 1L;

	public DateTimeSerializer() {
        this(null);
    }

    public DateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

	@Override
	public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider provider) throws IOException {
		generator.writeString(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
	}
	
}
