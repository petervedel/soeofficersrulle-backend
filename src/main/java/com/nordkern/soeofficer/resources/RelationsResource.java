package com.nordkern.soeofficer.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.nordkern.soeofficer.api.AuthenticatedUser;
import com.nordkern.soeofficer.api.Person;
import com.nordkern.soeofficer.api.Relation;
import com.nordkern.soeofficer.db.RelationsDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
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

/**
 * Created by mortenfrank on 24/11/2017.
 */
@Api(value = "/relations", description = "The persons of the system")
@Path("/relations")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class RelationsResource implements DummyObject {
    @Inject
    private RelationsDAO dao;

    @Inject
    private RelationsDAO relationDAO;
    public RelationsResource() {

    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "parent", value = "The parent of the relation", required = true, dataType = "object", paramType = "query"),
            @ApiImplicitParam(name = "child", value = "The child of the relation", required = true, dataType = "object", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "The title of the parent. I.e. {Mor,Far}", required = true, dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "Add a new relation to the system",
            response = Relation.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @PermitAll
    @POST
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Relation createRelation(@Valid Relation relation, @Auth AuthenticatedUser user) {
        return dao.create(relation);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "parent", value = "The parent of the relation", dataType = "object", paramType = "query"),
            @ApiImplicitParam(name = "child", value = "The child of the relation", dataType = "object", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "The title of the parent. I.e. {Mor,Far}", dataType = "string", paramType = "query")
    })
    @ApiOperation(value = "Update an existing relation of the system",
            response = Relation.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @PermitAll
    @PUT
    @Path("/{id}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Relation updateRelation(@PathParam("id") Integer id, @Valid Relation relation, @Auth AuthenticatedUser user) {
        try {
            relation.setId(new Long(id));
            dao.update(relation);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        return relation;
    }

    @Override
    public Relation getDummyObject() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateOfBirth = null;
        try {
            dateOfBirth = sdf.parse("01/01/1954");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Person dummyPersonChild = new Person();
        dummyPersonChild.setId(null);
        dummyPersonChild.setDateOfBirth(dateOfBirth);
        dummyPersonChild.setGivenName("John");
        dummyPersonChild.setSurname("Doe");
        dummyPersonChild.setGender(Person.Gender.Mand);

        Person dummyPersonParent = new Person();
        dummyPersonChild.setId(null);
        dummyPersonChild.setDateOfBirth(dateOfBirth);
        dummyPersonChild.setGivenName("Johnny");
        dummyPersonChild.setSurname("Does");
        dummyPersonChild.setGender(Person.Gender.Mand);

        Relation dummyRelation = new Relation();
        dummyRelation.setId(null);
        dummyRelation.setChild(dummyPersonChild);
        dummyRelation.setParent(dummyPersonChild);
        dummyRelation.setTitle(Relation.Title.Far);

        return dummyRelation;
    }
}
