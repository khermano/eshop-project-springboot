package cz.muni.fi.productService.openapiconfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenApiConfigs {
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${openapi.service.title}") String serviceTitle,
            @Value("${openapi.service.version}") String serviceVersion,
            @Value("${openapi.service.url}") String url) {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                //Swagger UI server has to be defined the way API Gateway recognize it thanks to the Eureka server and Feign Client!
                //that is why we need an application name here
                //we also added /eshop-rest into the API Gateway path so the path is as in original project
                .servers(List.of(new Server().url(url + "/eshop-rest/" + applicationName)))
                .info(new Info().title(serviceTitle).version(serviceVersion));
    }
}