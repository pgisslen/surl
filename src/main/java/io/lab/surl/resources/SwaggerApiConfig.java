package com.izettle.product.library.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.izettle.authorization.dto.Permission;
import io.swagger.annotations.Contact;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.OAuth2Definition;
import io.swagger.annotations.Scope;
import io.swagger.annotations.SecurityDefinition;
import io.swagger.annotations.SwaggerDefinition;

@SwaggerDefinition(
    info = @Info(
        title = "API documentation for iZettle Product Library",
        description = ""
            + "A product library is a representation of all the items that can be displayed, put in a shopping cart  "
            + "and sold to a customer. Items may be either producs or discounts.\n"
            + "\n"
            + "A product is a syntethic construct, wrapping one or more variants (which is the actual item being sold)"
            + ". Variants expresses different variations of properties such as for example price, size or color.\n"
            + "\n"
            + "A discount will reduce the total amount charged in a shopping cart. It can be used per item line, or on "
            + "the whole cart. It may reduce the affected amount by a percentage, or by a fixed amount.\n"
            + "\n"
            + "Together, the above types of entities makes up a complete library. The library can be fetched as a "
            + "whole through the library endpoint, where each consecutive change applied to the library is available. "
            + "Once the full library is retrieved, only later events needs to be fetched to keep the client up to date "
            + "with the server.\n"
            + "\n"
            + "All path patterns \"/organizations/{organizationUuid}/\" can be replaced by \"/organizations/self/\" "
            + "for convenience as all endpoints are for authorized users.",
        version = "v1.0",
        contact = @Contact(
            name = "iZettle Storefront team",
            email = "storefront@izettle.com",
            url = "http://www.izettle.com"
        )
    ),
    consumes = {APPLICATION_JSON},
    produces = {APPLICATION_JSON},
    schemes = {SwaggerDefinition.Scheme.HTTPS},
    securityDefinition = @SecurityDefinition(
        oAuth2Definitions = @OAuth2Definition(
            scopes = {
                @Scope(name = Permission.READ_PRODUCT, description = "Access to read product library data"),
                @Scope(name = Permission.WRITE_PRODUCT, description = "Access to modify product library data")
            },
            authorizationUrl = "https://oauth.izettle.net",
            description = "",
            flow = OAuth2Definition.Flow.IMPLICIT,
            key = "oauth2"
        )),
    externalDocs = @ExternalDocs(
        value = "iZettle at GitHub",
        url = "https://github.com/iZettle/api-documentation"))
interface SwaggerApiConfig {
}
