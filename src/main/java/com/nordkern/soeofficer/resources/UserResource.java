package com.nordkern.soeofficer.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.nordkern.soeofficer.api.AuthenticatedUser;
import com.nordkern.soeofficer.api.User;
import com.nordkern.soeofficer.api.UserLogin;
import com.nordkern.soeofficer.api.UserSearch;
import com.nordkern.soeofficer.core.MessageFactory;
import com.nordkern.soeofficer.db.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by mortenfrank on 24/11/2017.
 */
@Api(value = "/user", description = "The users of the system")
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class UserResource implements DummyObject {
    @Inject
    private UserDAO dao;

    public UserResource() {
    }

    @GET
    @Path("/{id}")
    @PermitAll
    @Timed
    @ApiOperation(value = "Gets specific user by id",
            response = User.class)
    @UnitOfWork
    public User readUser(@PathParam("id") LongParam id, @Auth AuthenticatedUser autheticatedUser) {
        User user = dao.findById(id.get());
        if (Objects.isNull(user)) {
            throw new WebApplicationException(
                    MessageFactory.getErrorMessage(
                            "USER_DOES_NOT_EXIST",
                            id.toString()),
                    Response.Status.NOT_ACCEPTABLE);
        }

        return user;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "The unique username of the user", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "The unique email of the user", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "The password for the user", required = true, dataType = "string", paramType = "query"),

            @ApiImplicitParam(name = "from", value = "The date from which the user is valid. Format is: DD/MM/YYYY", required = true, dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "until", value = "The date on which to user is invalid. Format is: DD/MM/YYYY", required = true, dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "role", value = "The system-role of the user, from the set {contributor, administrator, read, privileged_read}", required = true, dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "Add a new user to the system",
            response = User.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @POST
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public User createUser(@Valid User user, @Auth AuthenticatedUser autheticatedUser) {
        try {
            user =  dao.create(user);
        } catch (Exception e) {
            if (e instanceof MySQLIntegrityConstraintViolationException || e instanceof ConstraintViolationException) {
                throw new WebApplicationException(
                        MessageFactory.getErrorMessage(
                                "UNIQUE_CONSTRAINT_VIOLATED"),
                        Response.Status.BAD_REQUEST);
            } else {
                throw new WebApplicationException(e.getMessage(),
                        Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        return user;
    }

    @ApiOperation(value = "Get all users in the system matching the supplied criteria",
            response = User.class,
            responseContainer="List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "The username of the user", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "The email of the user", dataType = "string", paramType = "query")
    })
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @POST
    @Path("/search")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<User> getPersonsMatchingCriteria(@Valid UserSearch userSearch, @Auth AuthenticatedUser user) {
        return dao.findByCriteria(userSearch);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "The unique username of the user", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "The unique email of the user", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "The password for the user", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "from", value = "The date from which the user is valid. Format is: DD/MM/YYYY", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "until", value = "The date on which to user is invalid. Format is: DD/MM/YYYY", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "role", value = "The system-role of the user, from the set {contributor, administrator, read, privileged_read}", dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "Update an existing user of the system",
            response = User.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @PUT
    @Path("/{id}")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public User updateUser(@PathParam("id") Integer id, @Valid User user, @Auth AuthenticatedUser autheticatedUser) {
        try {
            user.setId(new Long(id));
            dao.update(user);
        } catch (Exception e) {
            if (e instanceof MySQLIntegrityConstraintViolationException || e instanceof ConstraintViolationException) {
                throw new WebApplicationException(
                        MessageFactory.getErrorMessage(
                                "UNIQUE_CONSTRAINT_VIOLATED"),
                        Response.Status.BAD_REQUEST);
            } else {
                throw new WebApplicationException(e.getMessage(),
                        Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        return user;
    }

    @ApiOperation(value = "Delete user from the system",
            response = Response.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @DELETE
    @Path("/{id}")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Integer id, @Auth AuthenticatedUser autheticatedUser) {
        try {
            dao.delete(id);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok().build();
    }

    @ApiOperation(value = "Get all users of the system",
            response = User.class,
            responseContainer="List")
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @GET
    @Path("/all")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers(@Auth AuthenticatedUser autheticatedUser) {
        return dao.findAll();
    }

    @ApiOperation(value = "Verify the password of a given user",
            response = User.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "The unique username of the user", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "The unique email of the user", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "The new password", dataType = "string", paramType = "query", required = true)
    })
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @POST
    @Path("/verify")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public User verifyUser(@Valid UserLogin userLogin, @Auth AuthenticatedUser autheticatedUser) throws Exception {
        User user = dao.verifyPasswordForUser(userLogin);
        if (Objects.isNull(user)) {
            throw new WebApplicationException(
                    MessageFactory.getErrorMessage(
                            "LOGIN_NOT_ACCEPTED"),
                    Response.Status.NOT_ACCEPTABLE);
        }

        return user;
    }

    @ApiOperation(value = "Update an user password",
            response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "The unique username of the user", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "The unique email of the user", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "The new password", dataType = "string", paramType = "query", required = true)
    })
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @POST
    @Path("/updatePassword")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePassword(@Valid UserLogin userLogin, @Auth AuthenticatedUser autheticatedUser) throws Exception {
        if (dao.setUserLogin(userLogin)) {
            return Response.ok().status(Response.Status.ACCEPTED).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Override
    public User getDummyObject() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateFrom = null;
        Date dateUntil = null;
        try {
            dateFrom = sdf.parse("01/01/2012");
            dateUntil = sdf.parse("01/01/2021");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        User dummyUser = new User();
        dummyUser.setFrom(dateFrom);
        dummyUser.setUntil(dateUntil);
        dummyUser.setUsername("JohnDoe");
        dummyUser.setRole(User.Role.administrator);
        dummyUser.setId(null);
        dummyUser.setEmail("john@doe.com");

        return dummyUser;
    }
}
