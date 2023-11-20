package eu.derzauberer.pis.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

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
import eu.derzauberer.pis.dto.UserEditDto;
import eu.derzauberer.pis.entity.Line;
import eu.derzauberer.pis.entity.Operator;
import eu.derzauberer.pis.entity.Route;
import eu.derzauberer.pis.entity.Station;
import eu.derzauberer.pis.entity.StationTraffic;
import eu.derzauberer.pis.entity.TransportationType;
import eu.derzauberer.pis.entity.User;
import eu.derzauberer.pis.repository.EntityRepository;
import eu.derzauberer.pis.repository.FileEntityRepository;
import eu.derzauberer.pis.repository.MemoryEntityRepository;

@Configuration
public class SpringConfiguration implements ApplicationContextAware, WebMvcConfigurer {
	
	public static boolean caching = true;
	public static boolean indexing = true;
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringConfiguration.applicationContext = applicationContext;
	}
	
	public static <T> T getBean(Class<T> bean) {
        return applicationContext.getBean(bean);
    }
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor()).excludePathPatterns("/api/**","/resources/**");
        registry.addInterceptor(somePageInterceptor()).excludePathPatterns("/api/**","/resources/**");
    }
    
    @Bean
    public HomePageInterceptor somePageInterceptor() {
        return new HomePageInterceptor();
    }
	
	@Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofStrict();
    }
	
    @Bean
    public MessageSource messageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
	
	@Bean
	public LocaleResolver localeResolver() {
	    return new CookieLocaleResolver();
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		final LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
	
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
		modelMapper.getConfiguration().setDeepCopyEnabled(true);
		return modelMapper;
	}
	
	@Bean
	public TypeMap<User, UserEditDto> getUserEditDtoModelMapper() {
		final TypeMap<User, UserEditDto> userModelMapper = getModelMapper().createTypeMap(User.class, UserEditDto.class);
		userModelMapper.addMappings(mapper -> mapper.skip(UserEditDto::setPassword));
		return userModelMapper;
	}
	
	@Bean
	public TypeMap<UserEditDto, User> getUserModelMapper() {
		final TypeMap<UserEditDto, User> userModelMapper = getModelMapper().createTypeMap(UserEditDto.class, User.class);
		userModelMapper.addMappings(mapper -> mapper.skip(User::setPassword));
		return userModelMapper;
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public EntityRepository<Line> getLineRepository() {
		return new FileEntityRepository<>("lines", Line.class);
	}
	
	@Bean
	public EntityRepository<Operator> getOperatorRepository() {
		return caching ? new MemoryEntityRepository<>("operators", Operator.class) : new FileEntityRepository<>("operators", Operator.class);
	}
	
	@Bean
	public EntityRepository<Route> getRouteRepository() {
		return caching ? new MemoryEntityRepository<>("routes", Route.class) : new FileEntityRepository<>("routes", Route.class);
	}
	
	@Bean
	public EntityRepository<Station> getStationRepository() {
		return caching ? new MemoryEntityRepository<>("stations", Station.class) : new FileEntityRepository<>("stations", Station.class);
	}
	
	@Bean
	public EntityRepository<StationTraffic> getStationTrafficRepository() {
		return new FileEntityRepository<>("station_traffic_indices", StationTraffic.class);
	}
	
	@Bean
	public EntityRepository<TransportationType> getTypeRepository() {
		return caching ? new MemoryEntityRepository<>("types", TransportationType.class) : new FileEntityRepository<>("types", TransportationType.class);
	}
	
	@Bean
	public EntityRepository<User> getUserRepository() {
		return caching ? new MemoryEntityRepository<>("users", User.class) : new FileEntityRepository<>("users", User.class);
	}
	
}
