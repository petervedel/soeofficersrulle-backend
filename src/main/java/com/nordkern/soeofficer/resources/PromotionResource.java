package com.nordkern.soeofficer.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.nordkern.soeofficer.api.*;
import com.nordkern.soeofficer.db.PromotionDAO;
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
@Api(value = "/promotion", description = "The promotions of the system")
@Path("/promotion")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class PromotionResource implements DummyObject {
    @Inject
    private PromotionDAO promotionDAO;

    public PromotionResource() {

    }

    @ApiOperation(value = "Add a promotion to the system",
            response = Promotion.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateOfPromotion", value = "The date from which the rank is valid", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(name = "rank", value = "The date from which the rank is invalid", dataType = "object", paramType = "query", required = true),
            @ApiImplicitParam(name = "officer", value = "The id of the officer possessing the rank", dataType = "object", paramType = "query", required = true)
    })
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @POST
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Promotion createPromotion(@Valid Promotion promotion, @Auth AuthenticatedUser user) {
        return promotionDAO.create(promotion);
    }

    @ApiOperation(value = "Update a promotion of the system",
            response = Promotion.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateOfPromotion", value = "The date from which the rank is valid", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "rank", value = "The date from which the rank is invalid", dataType = "object", paramType = "query"),
            @ApiImplicitParam(name = "officer", value = "The id of the officer possessing the rank", dataType = "object", paramType = "query")
    })
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @Path("/{id}")
    @PUT
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Promotion updatePromotion(@PathParam("id") Integer id, @Valid Promotion promotion, @Auth AuthenticatedUser user) {
        try {
            promotion.setId(new Long(id));
            promotionDAO.update(promotion);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        return promotion;
    }

    @ApiOperation(value = "Delete a promotion from the system",
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
            promotionDAO.delete(id);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok().build();
    }

    @Override
    public Promotion getDummyObject() {
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
        dummyPerson.setGender(Person.Gender.Mand);

        dummyOfficer.setPerson(dummyPerson);

        Date dateOfPromotion = null;
        try {
            dateOfPromotion = sdf.parse("01/01/1974");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date rankValidFrom = null;
        Date rankValidUntil = null;
        try {
            rankValidFrom = sdf.parse("01/01/1954");
            rankValidUntil = sdf.parse("01/01/1974");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Rank dummyRank = new Rank();
        dummyRank.setId(null);
        dummyRank.setNatoCode(123L);
        dummyRank.setRankName("Officer");
        dummyRank.setRankValidFrom(rankValidFrom);
        dummyRank.setRankValidUntil(rankValidUntil);

        Promotion dummyPromotion = new Promotion();
        dummyPromotion.setDateOfPromotion(dateOfPromotion);
        dummyPromotion.setId(null);
        dummyPromotion.setOfficer(dummyOfficer);
        dummyPromotion.setRank(dummyRank);

        return dummyPromotion;
    }
}
