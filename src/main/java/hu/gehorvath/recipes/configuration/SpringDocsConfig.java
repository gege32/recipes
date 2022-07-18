package hu.gehorvath.recipes.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class SpringDocsConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("recipe-public")
                .pathsToMatch("/recipe**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Recipe API")
                        .description("Gergo assigment project")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://github.com/gege32/recipes")))
                .externalDocs(new ExternalDocumentation()
                        .description("Gergo assigment project documentation")
                        .url("https://github.com/gege32/recipes"));
    }

}
