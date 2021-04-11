package pl.casmic.fileuploader.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ViewResolver getHtmlViewResolver() {
        final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/resources/templates/item/");
        return resolver;
    }

    @Bean
    public ViewResolver getJSONViewResolver() {
        final JsonViewResolver resolver = new JsonViewResolver();
        return resolver;
    }

//    @Bean
//    public Jackson2ObjectMapperBuilder jacksonBuilder() {
//        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//        builder.featuresToEnable(DeserializationFeature.UNWRAP_ROOT_VALUE);
//        return builder;
//    }

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(true)
                .parameterName("f")
                .ignoreAcceptHeader(true)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.TEXT_HTML)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);
        return resolver;
    }
}
