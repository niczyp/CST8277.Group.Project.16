/********************************************************************************************************
 * File:  MedicalTraining.java
 * Course Materials CST 8277
 *
 * @author Nicholas Zypchen
 * 
 */

package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.*;

import java.util.List;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalTraining;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path(MEDICAL_TRAINING_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicalTrainingResource {

    @EJB
    protected ACMEMedicalService service;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getAllMedicalTrainings() {
        List<MedicalTraining> trainingList = service.getAll(MedicalTraining.class, MedicalTraining.ALL_TRAINING_QUERY_NAME);
        return Response.ok(trainingList).build();
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getTrainingById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        MedicalTraining mt = service.getById(MedicalTraining.class, MedicalTraining.FIND_BY_ID, id);
        return (mt == null)
            ? Response.status(Status.NOT_FOUND).build()
            : Response.ok(mt).build();
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addMedicalTraining(MedicalTraining newTraining) {
        MedicalTraining saved = service.persistMedicalTraining(newTraining);
        return Response.status(Status.CREATED).entity(saved).build();
    }

    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response updateTraining(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, MedicalTraining updated) {
        MedicalTraining result = service.updateMedicalTraining(id, updated);
        return (result == null)
            ? Response.status(Status.NOT_FOUND).build()
            : Response.ok(result).build();
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteTraining(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        MedicalTraining deleted = service.deleteMedicalTraining(id);
        return (deleted == null)
            ? Response.status(Status.NOT_FOUND).build()
            : Response.noContent().build();
    }
}
