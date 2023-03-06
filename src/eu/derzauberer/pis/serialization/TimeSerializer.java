package eu.derzauberer.pis.serialization;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class TimeSerializer extends StdSerializer<LocalTime> {

	private static final long serialVersionUID = 1L;

	public TimeSerializer() {
        this(null);
    }

    public TimeSerializer(Class<LocalTime> t) {
        super(t);
    }

	@Override
	public void serialize(LocalTime value, JsonGenerator generator, SerializerProvider provider) throws IOException {
		generator.writeString(value.format(DateTimeFormatter.ofPattern("HH:mm")));
	}
	
}
