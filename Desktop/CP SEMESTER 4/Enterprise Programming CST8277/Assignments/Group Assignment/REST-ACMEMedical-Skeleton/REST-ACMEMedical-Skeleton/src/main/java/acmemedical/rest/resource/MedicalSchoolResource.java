/********************************************************************************************************
 * File:  MedicalSchoolResource.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * 
 */
package acmemedical.rest.resource;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;
import jakarta.ws.rs.core.Response.Status;
import static acmemedical.utility.MyConstants.MEDICAL_SCHOOL_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_SCHOOL_ID_PATH;
import static acmemedical.utility.MyConstants.SCHOOL_ID_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.MEDICAL_TRAINING_SUBRESOURCE_PATH;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalTraining;
import acmemedical.entity.MedicalSchool;

@Path(MEDICAL_SCHOOL_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicalSchoolResource {
    
    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @Inject
    protected SecurityContext sc;
    
    @GET
    public Response getMedicalSchools() {
        LOG.debug("Retrieving all medical schools...");
        List<MedicalSchool> medicalSchools = service.getAllMedicalSchools();
        LOG.debug("Medical schools found = {}", medicalSchools);
        Response response = Response.ok(medicalSchools).build();
        return response;
    }
    
    @GET
    // TODO MSR01 - Specify the roles allowed for this method
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_SCHOOL_ID_PATH)
    public Response getMedicalSchoolById(@PathParam(SCHOOL_ID_RESOURCE_NAME) int medicalSchoolId) {
        LOG.debug("Retrieving medical school with id = {}", medicalSchoolId);
        MedicalSchool medicalSchool = service.getMedicalSchoolById(medicalSchoolId);
        Response response = Response.ok(medicalSchool).build();
        return response;
    }

    @DELETE
    // TODO MSR02 - Specify the roles allowed for this method
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_SCHOOL_ID_PATH)
    public Response deleteMedicalSchool(@PathParam(SCHOOL_ID_RESOURCE_NAME) int msId) {
        LOG.debug("Deleting medical school with id = {}", msId);
        MedicalSchool sc = service.deleteMedicalSchool(msId); // Here we are using sc as a local variable and also for SecurityContext (which is globally injected).Fix: Potentially rename the local variable to something more meaningful.
        Response response = Response.ok(sc).build();
        return response;
    }
    
    // Please try to understand and test the below methods:
    @RolesAllowed({ADMIN_ROLE})
    @POST
    public Response addMedicalSchool(MedicalSchool newMedicalSchool) {
        LOG.debug("Adding a new medical school = {}", newMedicalSchool);
        if (service.isDuplicated(newMedicalSchool)) {
            HttpErrorResponse err = new HttpErrorResponse(Status.CONFLICT.getStatusCode(), "Entity already exists");
            return Response.status(Status.CONFLICT).entity(err).build();
        }
        else {
            MedicalSchool tempMedicalSchool = service.persistMedicalSchool(newMedicalSchool);
            return Response.ok(tempMedicalSchool).build();
        }
    }

    @RolesAllowed({ADMIN_ROLE})
    @POST
    @Path(MEDICAL_TRAINING_SUBRESOURCE_PATH)
    public Response addMedicalTrainingToMedicalSchool(@PathParam(SCHOOL_ID_RESOURCE_NAME) int msId, MedicalTraining newMedicalTraining) {
        LOG.debug("Adding a new MedicalTraining to medical school with id = {}", msId);
        
        MedicalSchool ms = service.getMedicalSchoolById(msId);
        newMedicalTraining.setMedicalSchool(ms);
        ms.getMedicalTrainings().add(newMedicalTraining);
        service.updateMedicalSchool(msId, ms);
        
        return Response.ok(sc).build(); // Here we are returning SecurityContext (sc), which makes no sense in a REST response. Fix: Return either the new MedicalTraining, or the updated MedicalSchool.
    }

    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @PUT
    @Path(RESOURCE_PATH_SCHOOL_ID_PATH)
    public Response updateMedicalSchool(@PathParam(SCHOOL_ID_RESOURCE_NAME) int msId, MedicalSchool updatingMedicalSchool) {
        LOG.debug("Updating a specific medical school with id = {}", msId);
        Response response = null;
        MedicalSchool updatedMedicalSchool = service.updateMedicalSchool(msId, updatingMedicalSchool);
        response = Response.ok(updatedMedicalSchool).build();
        return response;
    }
    
}
