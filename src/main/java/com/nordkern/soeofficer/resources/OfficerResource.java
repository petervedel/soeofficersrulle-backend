package com.nordkern.soeofficer.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.nordkern.soeofficer.api.*;
import com.nordkern.soeofficer.core.MessageFactory;
import com.nordkern.soeofficer.db.OfficerDAO;
import com.nordkern.soeofficer.db.PromotionDAO;
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
@Api(value = "/officer", description = "The officers of the system")
@Path("/officer")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class OfficerResource implements DummyObject {
    @Inject
    private OfficerDAO officerDAO;

    @Inject
    private PromotionDAO promotionDAO;

    @GET
    @Path("/{id}")
    @PermitAll
    @Timed
    @ApiOperation(value = "Get specific officer from id",
            response = Officer.class)
    @UnitOfWork
    public Officer readOfficer(@PathParam("id") LongParam id, @Auth AuthenticatedUser user) {
        Officer officer = officerDAO.findById(id.get(),true);
        if (Objects.isNull(officer)) {
            throw new WebApplicationException(
                    MessageFactory.getErrorMessage(
                            "OFFICER_DOES_NOT_EXIST",
                            id.toString()),
                    Response.Status.NOT_ACCEPTABLE);
        }

        return officer;
    }

    @GET
    @Path("/{id}/limited")
    @PermitAll
    @Timed
    @ApiOperation(value = "Get specific officer from id (limited)",
            response = Officer.class)
    @UnitOfWork
    public Officer readOfficerLimited(@PathParam("id") LongParam id, @Auth AuthenticatedUser user) {
        Officer officer = officerDAO.findById(id.get(),false);
        if (Objects.isNull(officer)) {
            throw new WebApplicationException(
                    MessageFactory.getErrorMessage(
                            "OFFICER_DOES_NOT_EXIST",
                            id.toString()),
                    Response.Status.NOT_ACCEPTABLE);
        }

        return officer;
    }

    @ApiOperation(value = "Promote an officer",
            response = Promotion.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @Path("/{id}/promote")
    @POST
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Promotion promoteOfficer(@PathParam("id") LongParam id, Promotion promotion, @Auth AuthenticatedUser user) {
        Promotion result = promotionDAO.promote(id.get(),promotion);
        if (Objects.isNull(result)) {
            throw new WebApplicationException(
                    MessageFactory.getErrorMessage(
                            "OFFICER_IS_ALREADY_CHAMP",
                            id.toString()),
                    Response.Status.NOT_ACCEPTABLE);
        }
        return result;
    }

    @ApiOperation(value = "Get all promotions belonging to an officer",
            response = Promotion.class,
            responseContainer = "List")
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @Path("/{id}/promotion/all")
    @GET
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Promotion> getPromotions(@PathParam("id") LongParam id, @Auth AuthenticatedUser user) {
        List<Promotion> promotions = promotionDAO.findPromotionsByOfficerId(id.get());

        return promotions;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "appointedNumber", value = "The appointed number of the officer", required = true, dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "dodabNumber", value = "The officer's Dodab number", dataType = "integer", required = true,paramType = "query"),
            @ApiImplicitParam(name = "appointedDate", value = "The date from which the officer title is valid", required = true, dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "appointedUntil", value = "The date from which the officer title is invalid", required = true, dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "terminationCause", value = "The cause of termination", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "person", required = true, dataType = "object", paramType = "query")
    })
    @ApiOperation(value = "Add an officer to the system",
            response = Officer.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @POST
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Officer createOfficer(@Valid Officer officer, @Auth AuthenticatedUser user) throws ParseException {
        try {
            return officerDAO.create(officer);
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
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "appointedNumber", value = "The appointed number of the officer", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "dodabNumber", value = "The officer's Dodab number", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "appointedDate", value = "The date from which the officer title is valid", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "appointedUntil", value = "The date from which the officer title is invalid", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "terminationCause", value = "The cause of termination",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "person", dataType = "object", paramType = "query")
    })
    @ApiOperation(value = "Update an existing officer of the system",
            response = Officer.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @PUT
    @Path("/{id}")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Officer updateOfficer(@PathParam("id") Integer id, @Valid Officer officer, @Auth AuthenticatedUser user) {
        try {
            officer.setId(new Long(id));
            officerDAO.update(officer);
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

        return officer;
    }

    @ApiOperation(value = "Delete an officer from the system",
            response = Response.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @DELETE
    @Path("/{id}")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteOfficer(@PathParam("id") Integer id, @Auth AuthenticatedUser user) {
        try {
            officerDAO.delete(id);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok().build();
    }

    @ApiOperation(value = "Get all officers matching the supplied criteria",
            response = Officer.class,
            responseContainer="List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "givenName", value = "The given name of the person", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "surname", value = "The surname of the person", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "dateOfBirth", value = "The person's date of birth. Format is: DD/MM/YYYY", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "The gender of the person. Accepted values are from the set {Mand,Kvinde,Ukendt}", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "appointedDate", value = "The date from which the officer title is valid", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "rankID", value = "The ID of the rank to be used as a criteria", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "dodabNumber", value = "The officer's Dodab number", dataType = "integer", paramType = "query"),
    })
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @POST
    @Path("/search")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Officer> getPersonsMatchingCriteria(@Valid OfficerSearch officerSearch, @Auth AuthenticatedUser user) {
        return officerDAO.findByCriteria(officerSearch,true);
    }

    @POST
    @Path("/search/limited")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Officer> getPersonsMatchingCriteriaLimited(@Valid OfficerSearch officerSearch, @Auth AuthenticatedUser user) {
        return officerDAO.findByCriteria(officerSearch,false);
    }

    @ApiOperation(value = "Get all officer's in the system",
            response = Person.class,
            responseContainer="List")
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @GET
    @Path("/all")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Officer> getAllOfficers(@Auth AuthenticatedUser user) {
        return officerDAO.findAll();
    }

    @Path("/activeOnDate/limited")
    @POST
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<OfficerCorps> getActiveOfficersOnDateLimited(@Valid OfficerOnDateParam param, @Auth AuthenticatedUser user) {

        return officerDAO.findOfficersActiveOnDate(param.getDate(),false);
    }

    @ApiOperation(value = "Get all active officers on date",
            response = OfficerCorps.class,
            responseContainer = "List")
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @Path("/activeOnDate")
    @POST
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<OfficerCorps> getActiveOfficersOnDate(@Valid OfficerOnDateParam param, @Auth AuthenticatedUser user) {

        return officerDAO.findOfficersActiveOnDate(param.getDate(),true);
    }

    @Override
    public Officer getDummyObject() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date appointedDate = null;
        Date appointedUntil = null;
        try {
            appointedDate = sdf.parse("01/01/1954");
            appointedUntil = sdf.parse("01/01/1974");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Officer dummyOfficer = new Officer();
        dummyOfficer.setAppointedNumber(007L);
        dummyOfficer.setDodabNumber(123L);
        dummyOfficer.setId(null);
        dummyOfficer.setAppointedDate(appointedDate);
        dummyOfficer.setAppointedUntil(appointedUntil);
        dummyOfficer.setId(null);

        Date dateOfBirth = null;
        try {
            dateOfBirth = sdf.parse("01/01/1954");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Person dummyPerson = new Person();
        dummyPerson.setId(null);
        dummyPerson.setDateOfBirth(dateOfBirth);
        dummyPerson.setGivenName("John");
        dummyPerson.setSurname("Doe");
        dummyPerson.setGender(Person.Gender.Male);

        dummyOfficer.setPerson(dummyPerson);

        return dummyOfficer;
    }
}
