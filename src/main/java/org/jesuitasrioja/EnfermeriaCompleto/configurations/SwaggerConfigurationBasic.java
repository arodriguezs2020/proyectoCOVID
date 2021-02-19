package org.jesuitasrioja.EnfermeriaCompleto.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfigurationBasic {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("org.jesuitasrioja.Enfermeria")).paths(PathSelectors.any()).build()
				.apiInfo(apiInfo());
	}

	@Bean
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("API REST Enfermería")
				.description("Este API RESP es sobre un proyecto de clase sobre el COVID.").version("0.1")
				.contact(new Contact("Álvaro", "url", "dam16.2020@gmail.com")).build();
	}
}
