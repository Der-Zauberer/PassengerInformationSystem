package eu.derzauberer.pis.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
import eu.derzauberer.pis.interceptor.FilterInterceptor;
import eu.derzauberer.pis.interceptor.HistoryInterceptor;
import eu.derzauberer.pis.interceptor.PasswordChangeInterceptor;
import eu.derzauberer.pis.interceptor.SessionUpdateInterceptor;
import eu.derzauberer.pis.persistence.EntityRepository;
import eu.derzauberer.pis.persistence.FileEntityRepository;
import eu.derzauberer.pis.persistence.MemoryEntityRepository;
import eu.derzauberer.pis.structure.model.LineModel;
import eu.derzauberer.pis.structure.model.OperatorModel;
import eu.derzauberer.pis.structure.model.RouteModel;
import eu.derzauberer.pis.structure.model.StationModel;
import eu.derzauberer.pis.structure.model.StationTrafficModel;
import eu.derzauberer.pis.structure.model.TransportationTypeModel;
import eu.derzauberer.pis.structure.model.UserModel;

@Configuration
public class SpringConfiguration implements ApplicationContextAware, WebMvcConfigurer {
	
	private static final String[] DEFAULT_EXCLUSION_PATTERN = { "/api/**","/resources/**" };
	
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
        registry.addInterceptor(localeChangeInterceptor()).excludePathPatterns(DEFAULT_EXCLUSION_PATTERN);
        registry.addInterceptor(sessionUpdateInterceptor()).excludePathPatterns(DEFAULT_EXCLUSION_PATTERN);
        registry.addInterceptor(passwordChangeInterceptor()).excludePathPatterns(DEFAULT_EXCLUSION_PATTERN);
        registry.addInterceptor(filterInterceptor()).addPathPatterns("/studio/*");
        registry.addInterceptor(historyInterceptor()).excludePathPatterns(DEFAULT_EXCLUSION_PATTERN);
    }
    
    @Bean
    public SessionUpdateInterceptor sessionUpdateInterceptor() {
        return new SessionUpdateInterceptor();
    }
    
    @Bean PasswordChangeInterceptor passwordChangeInterceptor() {
    	return new PasswordChangeInterceptor();
    }
    
    @Bean
    public FilterInterceptor filterInterceptor() {
        return new FilterInterceptor();
    }
    
    @Bean
    public HistoryInterceptor historyInterceptor() {
        return new HistoryInterceptor();
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
	public PasswordEncoder getPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public EntityRepository<LineModel> getLineRepository() {
		return new FileEntityRepository<>("lines", LineModel.class);
	}
	
	@Bean
	public EntityRepository<OperatorModel> getOperatorRepository() {
		return caching ? new MemoryEntityRepository<>("operators", OperatorModel.class) : new FileEntityRepository<>("operators", OperatorModel.class);
	}
	
	@Bean
	public EntityRepository<RouteModel> getRouteRepository() {
		return caching ? new MemoryEntityRepository<>("routes", RouteModel.class) : new FileEntityRepository<>("routes", RouteModel.class);
	}
	
	@Bean
	public EntityRepository<StationModel> getStationRepository() {
		return caching ? new MemoryEntityRepository<>("stations", StationModel.class) : new FileEntityRepository<>("stations", StationModel.class);
	}
	
	@Bean
	public EntityRepository<StationTrafficModel> getStationTrafficRepository() {
		return new FileEntityRepository<>("station_traffic_indices", StationTrafficModel.class);
	}
	
	@Bean
	public EntityRepository<TransportationTypeModel> getTypeRepository() {
		return caching ? new MemoryEntityRepository<>("types", TransportationTypeModel.class) : new FileEntityRepository<>("types", TransportationTypeModel.class);
	}
	
	@Bean
	public EntityRepository<UserModel> getUserRepository() {
		return caching ? new MemoryEntityRepository<>("users", UserModel.class) : new FileEntityRepository<>("users", UserModel.class);
	}
	
}
