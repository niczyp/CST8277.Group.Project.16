/********************************************************************************************************
 * File:  MedicineResource.java
 * Course Materials CST 8277
 * 
 * @author Nicholas Zypchen
 */
package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;
import static acmemedical.utility.MyConstants.MEDICINE_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_PATH;

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
import acmemedical.entity.Medicine;

@Path(MEDICINE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicineResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    public Response getAllMedicines() {
        LOG.debug("Getting all medicines...");
        List<Medicine> meds = service.getAll(Medicine.class, "Medicine.findAll");
        return Response.ok(meds).build();
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getMedicineById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Getting medicine by ID = {}", id);
        Medicine med = service.getById(Medicine.class, "Medicine.findById", id);
        return (med != null) ? Response.ok(med).build() : Response.status(Status.NOT_FOUND).build();
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addMedicine(Medicine newMed) {
        LOG.debug("Adding new medicine: {}", newMed);
        service.persistMedicine(newMed);
        return Response.status(Status.CREATED).entity(newMed).build();
    }

    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response updateMedicine(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, Medicine updatedMed) {
        LOG.debug("Updating medicine ID = {}", id);
        Medicine updated = service.updateMedicine(id, updatedMed);
        return (updated != null) ? Response.ok(updated).build() : Response.status(Status.NOT_FOUND).build();
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteMedicine(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Deleting medicine ID = {}", id);
        Medicine deleted = service.deleteMedicine(id);
        return (deleted != null) ? Response.noContent().build() : Response.status(Status.NOT_FOUND).build();
    }
}
