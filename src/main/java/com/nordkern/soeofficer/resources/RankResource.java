package com.nordkern.soeofficer.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.nordkern.soeofficer.api.AuthenticatedUser;
import com.nordkern.soeofficer.api.Rank;
import com.nordkern.soeofficer.core.MessageFactory;
import com.nordkern.soeofficer.db.RankDAO;
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
@Api(value = "/rank", description = "The ranks of the system")
@Path("/rank")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class RankResource implements DummyObject {
    @Inject
    private RankDAO dao;
    public RankResource() {

    }

    @GET
    @Path("/{id}")
    @PermitAll
    @Timed
    @ApiOperation(value = "Get specific rank from id",
            response = Rank.class)
    @UnitOfWork
    public Rank readRank(@PathParam("id") LongParam id, @Auth AuthenticatedUser user) {
        Rank rank = dao.findById(id.get());
        if (Objects.isNull(rank)) {
            throw new WebApplicationException(
                    MessageFactory.getErrorMessage(
                            "RANK_DOES_NOT_EXIST",
                            id.toString()),
                    Response.Status.NOT_ACCEPTABLE);
        }

        return rank;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "natoCode", value = "The nato code associated with the specific rank", required = true, dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "rankName", value = "The name of the rank", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "rankValidFrom", value = "The date from which the rank is valid", required = true, dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "rankValidUntil", value = "The date from which the rank is invalid", required = true, dataType = "date", paramType = "query")
    })
    @ApiOperation(value = "Add a rank to the system",
            response = Rank.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @POST
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Rank createRank(@Valid Rank rank, @Auth AuthenticatedUser user) {
        return dao.create(rank);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "natoCode", value = "The nato code associated with the specific rank", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "rankName", value = "The name of the rank", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "rankValidFrom", value = "The date from which the rank is valid", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "rankValidUntil", value = "The date from which the rank is invalid", dataType = "date", paramType = "query")
    })
    @ApiOperation(value = "Update an existing rank of the system",
            response = Rank.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @PUT
    @Path("/{id}")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Rank updateRank(@PathParam("id") Integer id, @Valid Rank rank, @Auth AuthenticatedUser user) {
        try {
            rank.setId(new Long(id));
            dao.update(rank);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        return rank;
    }

    @ApiOperation(value = "Delete a rank from the system",
            response = Response.class)
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @DELETE
    @Path("/{id}")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteRank(@PathParam("id") Integer id, @Auth AuthenticatedUser user) {
        try {
            dao.delete(id);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        return Response.ok().build();
    }

    @ApiOperation(value = "Get all ranks's in the system",
            response = Rank.class,
            responseContainer="List")
    @JsonParseFailure(swaggerLink = "https://path.to.swagger")
    @GET
    @Path("/all")
    @PermitAll
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Rank> getAllRanks(@Auth AuthenticatedUser user) {
        return dao.findAll();
    }

    @Override
    public Rank getDummyObject() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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

        return dummyRank;
    }
}
