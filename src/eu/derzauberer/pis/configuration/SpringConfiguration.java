package eu.derzauberer.pis.configuration;

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

import eu.derzauberer.pis.configuration.SerializationConfiguration.DateDeserializer;
import eu.derzauberer.pis.configuration.SerializationConfiguration.DateSerializer;
import eu.derzauberer.pis.configuration.SerializationConfiguration.DateTimeDeserializer;
import eu.derzauberer.pis.configuration.SerializationConfiguration.DateTimeSerializer;
import eu.derzauberer.pis.configuration.SerializationConfiguration.PrettyPrinter;
import eu.derzauberer.pis.configuration.SerializationConfiguration.TimeDeserializer;
import eu.derzauberer.pis.configuration.SerializationConfiguration.TimeSerializer;
import eu.derzauberer.pis.model.Line;
import eu.derzauberer.pis.model.Route;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.model.StationTraffic;
import eu.derzauberer.pis.model.TrainOperator;
import eu.derzauberer.pis.model.TrainType;
import eu.derzauberer.pis.model.User;
import eu.derzauberer.pis.repositories.FileRepository;
import eu.derzauberer.pis.repositories.MemoryRepository;
import eu.derzauberer.pis.repositories.Repository;

@Configuration
public class SpringConfiguration {

	@Bean
	public Jackson2ObjectMapperBuilder getJsonMapperBuilder() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(LocalDate.class, new DateSerializer());
		module.addDeserializer(LocalDate.class, new DateDeserializer());
		module.addSerializer(LocalTime.class, new TimeSerializer());
		module.addDeserializer(LocalTime.class, new TimeDeserializer());
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
	
	@Bean
	public ObjectMapper getObjectMapper() {
		final ObjectMapper objectMapper = getJsonMapperBuilder().build();
		objectMapper.setDefaultPrettyPrinter(new PrettyPrinter());
		return objectMapper;
	}
	
	@Bean
	public ModelMapper getModelMapper() {
		final ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		return modelMapper;
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public Repository<Line> getLineRepository() {
		return new FileRepository<>("lines", Line.class);
	}
	
	@Bean
	public Repository<TrainOperator> getOperatorRepository() {
		return new MemoryRepository<>("operators", TrainOperator.class);
	}
	
	@Bean
	public Repository<Route> getRouteRepository() {
		return new MemoryRepository<>("routes", Route.class);
	}
	
	@Bean
	public Repository<Station> getStationRepository() {
		return new MemoryRepository<>("stations", Station.class);
	}
	
	@Bean
	public Repository<StationTraffic> getStationTrafficRepository() {
		return new FileRepository<>("station_traffic_indices", StationTraffic.class);
	}
	
	@Bean
	public Repository<TrainType> getTypeRepository() {
		return new MemoryRepository<>("types", TrainType.class);
	}
	
	@Bean
	public Repository<User> getUserRepository() {
		return new MemoryRepository<>("users", User.class);
	}
	
}
