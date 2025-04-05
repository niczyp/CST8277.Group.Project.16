/********************************************************************************************************
 * File:  PatientResource.java
 * Course Materials CST 8277
 *
 * @author Nicholas :)
 * 
 */
package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;
import static acmemedical.utility.MyConstants.PATIENT_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_PATH;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
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
import jakarta.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.Patient;

@Path(PATIENT_RESOURCE_NAME) // /patient
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PatientResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getAllPatients() {
        LOG.debug("Getting all patients...");
        List<Patient> patients = service.getAll(Patient.class, "Patient.findAll");
        return Response.ok(patients).build();
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH) // /{id}
    public Response getPatientById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Getting patient by id = {}", id);
        Patient patient = service.getById(Patient.class, "Patient.findById", id);
        if (patient == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(patient).build();
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addPatient(Patient newPatient) {
        LOG.debug("Adding new patient: {}", newPatient);
        service.persistPatient(newPatient);
        return Response.status(Status.CREATED).entity(newPatient).build();
    }

    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response updatePatient(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, Patient updatedPatient) {
        LOG.debug("Updating patient with id = {}", id);
        Patient result = service.updatePatient(id, updatedPatient);
        if (result == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(result).build();
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deletePatient(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Deleting patient with id = {}", id);
        Patient deleted = service.deletePatient(id);
        if (deleted == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}