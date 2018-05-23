package com.nordkern.soeofficer;

/**
 * Created by mortenfrank on 18/12/2017.
 */
import com.google.inject.Inject;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Provider to provide the following to Jersey framework:</p>
 * <ul>
 * <li>Provision of general runtime exception to response mapping</li>
 * </ul>
 */
@Provider
@Slf4j
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Inject
    private Environment environment;

    @Override
    public Response toResponse(RuntimeException runtime) {

        // Build default response
        Response defaultResponse = Response
                .serverError()
                .build();

        // Check for any specific handling
        if (runtime instanceof WebApplicationException) {

            return handleWebApplicationException(runtime, defaultResponse);
        }

        // Use the default
        log.error(runtime.getMessage());
        return defaultResponse;

    }

    private Response handleWebApplicationException(RuntimeException exception, Response defaultResponse) {
        WebApplicationException webAppException = (WebApplicationException) exception;

        // No logging
        if (webAppException.getResponse().getStatus() == 400) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new SupplyMessageWithError(Response.Status.BAD_REQUEST.getStatusCode(), exception.getMessage(), "http://www.path.to.swagger"))
                    .build();
        }
        if (webAppException.getResponse().getStatus() == 401) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }
        if (webAppException.getResponse().getStatus() == 404) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new EndpointNotExistsError(Response.Status.NOT_FOUND.getStatusCode(), "The requested resource does not exist", "http://www.path.to.swagger",getResourceUrls()))
                    .build();
        }
        if (webAppException.getResponse().getStatus() == 405) {
            return Response
                    .status(Response.Status.METHOD_NOT_ALLOWED)
                    .entity(new EndpointNotExistsError(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), "The requested resource does not exist", "http://www.path.to.swagger",getResourceUrls()))
                    .build();
        }
        if (webAppException.getResponse().getStatus() == 406) {
            return Response
                    .status(Response.Status.NOT_ACCEPTABLE)
                    .entity(new SupplyMessageWithError(Response.Status.NOT_ACCEPTABLE.getStatusCode(), exception.getMessage(), "http://www.path.to.swagger"))
                    .build();
        }
        if (webAppException.getResponse().getStatus() > 499) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new SupplyMessageWithError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), exception.getMessage(), "http://www.path.to.swagger"))
                    .build();
        }

        return defaultResponse;
    }

    public List<Endpoint> getResourceUrls()
    {
        DropwizardResourceConfig config = new DropwizardResourceConfig();
        config.packages("com.nordkern.soeofficer");
        List<Endpoint> endpoints = new ArrayList<>();
        Endpoint endpoint;
        String[] infoString = config.getEndpointsInfo().split("\n");
        for (String info : infoString) {
            endpoint = new Endpoint();
            if (info.contains("POST")) {
                endpoint.setType("POST");
                endpoint.setEndpoint("..."+info.split("\\(")[0].split("POST")[1].trim());
                endpoints.add(endpoint);
            } else if (info.contains("PUT")) {
                endpoint.setType("PUT");
                endpoint.setEndpoint("..."+info.split("\\(")[0].split("PUT")[1].trim());
                endpoints.add(endpoint);
            } else if (info.contains("GET")) {
                endpoint.setType("GET");
                endpoint.setEndpoint("..."+info.split("\\(")[0].split("GET")[1].trim());
                endpoints.add(endpoint);
            } else if (info.contains("DELETE")) {
                endpoint.setType("DELETE");
                endpoint.setEndpoint("..."+info.split("\\(")[0].split("DELETE")[1].trim());
                endpoints.add(endpoint);
            }
        }
        return endpoints;

    }

}
