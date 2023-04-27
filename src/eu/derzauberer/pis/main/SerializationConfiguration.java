package eu.derzauberer.pis.main;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class SerializationConfiguration {
	
	public static class DateSerializer extends StdSerializer<LocalDate> {

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
	
	public static class DateDeserializer extends StdDeserializer<LocalDate> {

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
	
	public static class TimeSerializer extends StdSerializer<LocalTime> {

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
	
	public static class TimeDeserializer extends StdDeserializer<LocalTime> {

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
	
	public static class DateTimeSerializer extends StdSerializer<LocalDateTime> {

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
	
	public static class DateTimeDeserializer extends StdDeserializer<LocalDateTime> {

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

}
