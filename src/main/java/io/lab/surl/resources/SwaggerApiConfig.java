package io.lab.surl.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@SwaggerDefinition(
    info = @Info(
        title = "API documentation URL Shorterner Service",
        description = "Transforms a long URL into a tiny one.",
        version = "v1.0",
        contact = @Contact(
            name = "Petter",
            email = "petter@gisslen.com",
            url = "www.gisslen.com/petter"
        )
    ),
    consumes = {APPLICATION_JSON},
    produces = {APPLICATION_JSON})
interface SwaggerApiConfig {
}
