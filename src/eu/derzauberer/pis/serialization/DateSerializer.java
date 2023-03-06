package eu.derzauberer.pis.serialization;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DateSerializer extends StdSerializer<LocalDate> {

	private static final long serialVersionUID = 1L;

	public DateSerializer() {
        this(null);
    }

    public DateSerializer(Class<LocalDate> t) {
        super(t);
    }

	@Override
	public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider) throws IOException {
		generator.writeString(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}
	
}
