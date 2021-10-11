package io.mosip.kernel.masterdata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

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
	
	private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

	@Autowired
	private OpenApiProperties openApiProperties;
	
	@Bean
    public OpenAPI openApi() {
		String msg = "Swagger open api, ";
		OpenAPI api = new OpenAPI()
                .components(new Components());
		if (null != openApiProperties.getInfo()) {
			api.info(new Info()
				.title(openApiProperties.getInfo().getTitle())
				.version(openApiProperties.getInfo().getVersion())
				.description(openApiProperties.getInfo().getDescription()));
			if (null != openApiProperties.getInfo().getLicense()) {
				api.getInfo().license(new License()
						.name(openApiProperties.getInfo().getLicense().getName())
						.url(openApiProperties.getInfo().getLicense().getUrl()));
				logger.info("{} info license property is added", msg);
			} else {
				logger.error("{} info license property is empty", msg);
			}
			logger.info("{} info property is added", msg);
		} else {
			logger.error("{} info property is empty", msg);
		}
		
		if (null != openApiProperties.getService() && null != openApiProperties.getService().getServers()) {
			openApiProperties.getService().getServers().forEach(server -> {
				api.addServersItem(new Server().description(server.getDescription()).url(server.getUrl()));
			});
			logger.info("{} server property is added", msg);
		} else {
			logger.error("{} server property is empty", msg);
		}
		return api;
    }
	
}
