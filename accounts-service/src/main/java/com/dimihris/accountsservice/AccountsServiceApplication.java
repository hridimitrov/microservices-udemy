package com.dimihris.accountsservice;

import com.dimihris.accountsservice.dto.AccountContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = AccountContactInfoDto.class)
@EnableFeignClients
@OpenAPIDefinition(
        info = @Info(
                title = "Accounts microservice REST API Documentation",
                description = "Accounts microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Company Name",
                        email = "company@domain.com",
                        url = "https://example.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://example.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Accounts microservice REST API Documentation",
                url = "https://www.accountsmicroservice.com/swagger-ui/html"
        )
)
public class AccountsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsServiceApplication.class, args);
    }

}
