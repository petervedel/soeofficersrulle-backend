package com.nordkern.soeofficer.core.JSon;

import com.nordkern.soeofficer.core.MathFactory;
import com.nordkern.soeofficer.core.NoMatchException;
import com.nordkern.soeofficer.resources.DummyObject;
import com.nordkern.soeofficer.resources.JsonParseFailure;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.dropwizard.jersey.errors.LoggingExceptionMapper;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Collection;

/**
 * Created by mortenfrank on 14/12/2017.
 */

@Provider
@Slf4j
public class CustomJSonProcessingExceptionMapper extends LoggingExceptionMapper<JsonProcessingException> {

    @Context private ResourceInfo resourceInfo;

    @Override
    public Response toResponse(JsonProcessingException exception) {
        /*
         * If the error is in the JSON generation or an invalid definition, it's a server error.
         */
        if (exception instanceof JsonGenerationException || exception instanceof InvalidDefinitionException) {
            return super.toResponse(exception); // LoggingExceptionMapper will log exception
        }

        /*
         * Otherwise, it's those pesky users.
         */
        log.debug("Unable to process JSON", exception);

        final JsonParseFailureErrorMessage message = new JsonParseFailureErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),
                getParseFailureErrorMessage(exception), getParseFailureDocumentationLink(), getParseFailureObject());
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(message)
                .build();
    }

    private String getParseFailureErrorMessage(JsonProcessingException exception) {
        String message = "Unable to parse JSON";
        Method method = resourceInfo.getResourceMethod();

        if (method.isAnnotationPresent(JsonParseFailure.class)) {
            if (exception instanceof UnrecognizedPropertyException) {
                message = "The attribute '"+exception.getLocalizedMessage().split("\"")[1]+"' does not exist'.";
                if (getClosestMatch(exception) != null) {
                    message += " did you mean: '" + getClosestMatch(exception) + "'?";
                }
            } else {
                message = "The provided JSON is invalid!";
            }
        }

        return message;
    }

    private String getParseFailureDocumentationLink() {
        String documentationLink = null;
        Method method = resourceInfo.getResourceMethod();

        if (method.isAnnotationPresent(JsonParseFailure.class)) {
            JsonParseFailure annotation = method.getAnnotation(JsonParseFailure.class);
            documentationLink = annotation.swaggerLink();
        }
        return documentationLink;
    }

    private Object getParseFailureObject() {
        Method method = resourceInfo.getResourceMethod();
        Object object = null;

        if (method.isAnnotationPresent(JsonParseFailure.class)) {
            try {
                object = ((DummyObject) method.getDeclaringClass().newInstance()).getDummyObject();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return object;
    }

    private String getClosestMatch(JsonProcessingException exception) {
        log.debug("UnrecognizedPropertyException, attempting to find closest match");

        Collection<Object> coll = ((UnrecognizedPropertyException)exception).getKnownPropertyIds();
        String[] list = coll.toArray(new String[coll.size()]);

        try {
            return MathFactory.Levenshtein.getMatchWithShortestDistance(exception.getLocalizedMessage().split("\"")[1],list);
        } catch (NoMatchException e) {
            return null;
        }
    }

}