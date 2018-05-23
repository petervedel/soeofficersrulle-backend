package com.nordkern.soeofficer.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.nordkern.soeofficer.api.*;
import com.nordkern.soeofficer.core.MessageFactory;
import com.nordkern.soeofficer.db.PersonDAO;
import com.nordkern.soeofficer.db.RelationsDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

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
@Api(value = "/person", description = "The persons of the system")
@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class PersonResource implements DummyObject {
    @Inject
    private PersonDAO dao;

    @Inject
    private RelationsDAO relationDAO;
    public PersonResource() {

    }

    @ApiOperation(value = "Upload a CSV file containing persons to be inserted into the database",
            response = Person.class)
    @POST
    @Path("/upload")
    @PermitAll
    @Timed
    @UnitOfWork
    public Response upload(@Valid CSVFile file, @Auth AuthenticatedUser user) {
        try {
            dao.processFile(file);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok().build();
    }

    @ApiOperation(value = "Gets specific person by id",
            response = Person.class)
    @GET
    @Path("/{id}")
    @PermitAll
    @Timed
    @UnitOfWork
    public Person readPerson(@PathParam("id") LongParam id, @Auth AuthenticatedUser user) {
        Person person = dao.findById(id.get());
        if (Objects.isNull(person)) {
            throw new WebApplicationException(
                    MessageFactory.getErrorMessage(
                            "PERSON_DOES_NOT_EXIST",
                            id.toString()),
                    Response.Status.NOT_ACCEPTABLE);
        }

        return person;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "givenName", value = "The given name of the person", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "surname", value = "The surname of the person", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "dateOfBirth", value = "The person's date of birth. Format is: DD/MM/YYYY", required = true, dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "The gender of the person. Accepted values are from the set {Mand,Kvinde,Ukendt}", required = true, dataType = "string", paramType = "query"),@ApiImplicitParam(name = "dateOfDeath", value = "The date the person has dies", dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "Add a new person to the system",
            response = Person.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @PermitAll
    @POST
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Person createPerson(@Valid Person person, @Auth AuthenticatedUser user) {
        return dao.create(person);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "givenName", value = "The given name of the person", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "surname", value = "The surname of the person", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "dateOfBirth", value = "The person's date of birth. Format is: DD/MM/YYYY", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "The gender of the person. Accepted values are from the set {Mand,Kvinde,Ukendt}", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "dateOfDeath", value = "The date the person has dies", dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "Update an existing person of the system",
            response = Person.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @PermitAll
    @PUT
    @Path("/{id}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Person updatePerson(@PathParam("id") Integer id, @Valid Person person, @Auth AuthenticatedUser user) {
        try {
            person.setId(new Long(id));
            dao.update(person);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        return person;
    }

    @ApiOperation(value = "Delete person from the system",
            response = Response.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @DELETE
    @Path("/{id}")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePerson(@PathParam("id") Integer id, @Auth AuthenticatedUser user) {
        try {
            dao.delete(id);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok().build();
    }

    @ApiOperation(value = "Get all persons in the system matching the supplied criteria",
            response = Person.class,
            responseContainer="List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "givenName", value = "The given name of the person", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "surname", value = "The surname of the person", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "dateOfBirth", value = "The person's date of birth. Format is: DD/MM/YYYY", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "The gender of the person. Accepted values are from the set {Mand,Kvinde,Ukendt}", dataType = "string", paramType = "query")
    })
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @POST
    @Path("/search")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Person> getPersonsMatchingCriteria(@Valid PersonSearch personSearch, @Auth AuthenticatedUser user) {
        return dao.findByCriteria(personSearch);
    }

    @ApiOperation(value = "Get all persons in the system",
            response = Person.class,
            responseContainer="List")
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @GET
    @Path("/all")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Person> getAllPersons(@Auth AuthenticatedUser user) {
        return dao.findAll();
    }

    @GET
    @Path("/{id}/parents")
    @PermitAll
    @Timed
    @ApiOperation(value = "Gets parents of person",
            response = Relation.class,
            responseContainer = "List")
    @UnitOfWork
    public List<Relation> readParents(@PathParam("id") LongParam id, @Auth AuthenticatedUser autheticatedUser) {
        List<Relation> relations = relationDAO.findParentsOfPerson(id.get());

        return relations;
    }

    @GET
    @Path("/{id}/children")
    @PermitAll
    @Timed
    @ApiOperation(value = "Gets children of person",
            response = Relation.class,
            responseContainer = "List")
    @UnitOfWork
    public List<Relation> readChildren(@PathParam("id") LongParam id, @Auth AuthenticatedUser autheticatedUser) {
        List<Relation> relations = relationDAO.findChildrenOfPerson(id.get());

        return relations;
    }

    @Override
    public Person getDummyObject() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
        dummyPerson.setGender(Person.Gender.Mand);

        return dummyPerson;
    }
}
