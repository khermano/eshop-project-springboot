package cz.muni.fi.orderService.openapiconfig;

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
    @Bean
    public OpenAPI customOpenAPI(
            @Value("${openapi.service.title}") String serviceTitle,
            @Value("${openapi.service.version}") String serviceVersion,
            @Value("${openapi.service.url}") String url) {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                //Swagger UI server has to be defined the way API Gateway recognize it thanks to the Eureka server and Feign Client!
                //that is why we need to hardcode application name here
                .servers(List.of(new Server().url(url + "/orders/")))
                .info(new Info().title(serviceTitle).version(serviceVersion));
    }
}