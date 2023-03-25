package eu.derzauberer.pis.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import eu.derzauberer.pis.serialization.DateDeserializer;
import eu.derzauberer.pis.serialization.DateSerializer;
import eu.derzauberer.pis.serialization.DateTimeDeserializer;
import eu.derzauberer.pis.serialization.DateTimeSerializer;
import eu.derzauberer.pis.serialization.TimeDeserializer;
import eu.derzauberer.pis.serialization.TimeSerializer;

@Configuration
public class SpringConfiguration {
	
	private static ObjectMapper MAPPER;

	@Bean
	public Jackson2ObjectMapperBuilder getJsonMapperBuilder() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(LocalTime.class, new TimeSerializer());
		module.addDeserializer(LocalTime.class, new TimeDeserializer());
		module.addSerializer(LocalDate.class, new DateSerializer());
		module.addDeserializer(LocalDate.class, new DateDeserializer());
		module.addSerializer(LocalDateTime.class, new DateTimeSerializer());
		module.addDeserializer(LocalDateTime.class, new DateTimeDeserializer());
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.modules(module);
		builder.failOnUnknownProperties(false);
		builder.serializationInclusion(Include.NON_NULL);
		builder.visibility(PropertyAccessor.FIELD, Visibility.ANY);
		builder.visibility(PropertyAccessor.GETTER, Visibility.NONE);
		builder.visibility(PropertyAccessor.SETTER, Visibility.NONE);
		builder.visibility(PropertyAccessor.CREATOR, Visibility.ANY);
	    return builder;
	}
	
	public ObjectMapper getObjectMapper() {
		if (MAPPER == null) {
			MAPPER = getJsonMapperBuilder().build();
			MAPPER.setDefaultPrettyPrinter(new PrettyPrinter());
		}
		return MAPPER;
	}
	
	@Bean
	public ModelMapper getModelMapper() {
		final ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setSkipNullEnabled(true);
		return mapper;
	}
	
	@Bean 
	public PasswordEncoder getPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
}
