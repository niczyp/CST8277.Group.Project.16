/********************************************************************************************************
 * File:  PrescriptionResource.java
 * Course Materials CST 8277
 *
 * @author Nicholas
 */
package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;
import static acmemedical.utility.MyConstants.PRESCRIPTION_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.PRESCRIPTION_RESOURCE_PATH_ID_PATH;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.Prescription;
import acmemedical.entity.PrescriptionPK;

@Path(PRESCRIPTION_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PrescriptionResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    // GET all prescriptions
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getAllPrescriptions() {
        LOG.debug("Retrieving all prescriptions...");
        List<Prescription> prescriptions = service.getAll(Prescription.class, "Prescription.findAll");
        return Response.ok(prescriptions).build();
    }

    // GET by composite key
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(PRESCRIPTION_RESOURCE_PATH_ID_PATH)
    public Response getPrescriptionById(@PathParam("physician_id") int physicianId, @PathParam("patient_id") int patientId) {
        LOG.debug("Getting prescription for physicianId={} and patientId={}", physicianId, patientId);
        PrescriptionPK pk = new PrescriptionPK(physicianId, patientId);
        Prescription p = service.getPrescriptionByPK(pk);
        return (p != null) ? Response.ok(p).build() : Response.status(Status.NOT_FOUND).build();
    }

    // POST (create)
    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addPrescription(Prescription newPrescription) {
        LOG.debug("Adding new prescription = {}", newPrescription);
        Prescription created = service.persistPrescription(newPrescription);
        return Response.status(Status.CREATED).entity(created).build();
    }

    // PUT (update)
    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Path(PRESCRIPTION_RESOURCE_PATH_ID_PATH)
    public Response updatePrescription(@PathParam("physician_id") int physicianId, @PathParam("patient_id") int patientId, Prescription updated) {
        LOG.debug("Updating prescription with physicianId={} and patientId={}", physicianId, patientId);
        PrescriptionPK pk = new PrescriptionPK(physicianId, patientId);
        updated.setId(pk);
        Prescription updatedResult = service.updatePrescription(pk, updated);
        return (updatedResult != null) ? Response.ok(updatedResult).build() : Response.status(Status.NOT_FOUND).build();
    }

    // DELETE
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(PRESCRIPTION_RESOURCE_PATH_ID_PATH)
    public Response deletePrescription(@PathParam("physician_id") int physicianId, @PathParam("patient_id") int patientId) {
        LOG.debug("Deleting prescription with physicianId={} and patientId={}", physicianId, patientId);
        PrescriptionPK pk = new PrescriptionPK(physicianId, patientId);
        Prescription deleted = service.deletePrescription(pk);
        return (deleted != null) ? Response.noContent().build() : Response.status(Status.NOT_FOUND).build();
    }
}