package io.mosip.kernel.masterdata.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Configuration class for swagger config
 * 
 * @author Dharmesh Khandelwal
 * @since 1.0.0
 * @author Govindaraj Velu
 * @implSpec upgrade the Swagger2.0 to OpenAPI (Swagger3.0)
 *
 */
@Configuration
public class SwaggerConfig {

	@Autowired
	private OpenApiProperties openApiProperties;
	
	@Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title(openApiProperties.getInfo().getTitle())
                		.version(openApiProperties.getInfo().getVersion())
                		.description(openApiProperties.getInfo().getDescription())
                		.license(new License().name(openApiProperties.getInfo().getLicense().getName())
                				.url(openApiProperties.getInfo().getLicense().getUrl())));
    }

}
