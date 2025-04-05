/********************************************************************************************************
 * File:  MedicalCertificateResource.java
 * Course Materials CST 8277
 *
 * @author Nicholas Zypchen
 */
package acmemedical.rest.resource;

import static acmemedical.utility.MyConstants.ADMIN_ROLE;
import static acmemedical.utility.MyConstants.USER_ROLE;
import static acmemedical.utility.MyConstants.MEDICAL_CERTIFICATE_RESOURCE_NAME;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmemedical.utility.MyConstants.RESOURCE_PATH_ID_PATH;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalCertificate;

@Path(MEDICAL_CERTIFICATE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicalCertificateResource {

    private static final Logger LOG = LogManager.getLogger();

    @EJB
    protected ACMEMedicalService service;

    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getAllCertificates() {
        LOG.debug("Getting all medical certificates...");
        List<MedicalCertificate> certs = service.getAll(MedicalCertificate.class, "MedicalCertificate.findAll");
        return Response.ok(certs).build();
    }

    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getCertificateById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Getting certificate by id = {}", id);
        MedicalCertificate cert = service.getById(MedicalCertificate.class, "MedicalCertificate.findById", id);
        if (cert == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(cert).build();
    }

    @POST
    @RolesAllowed({ADMIN_ROLE})
    public Response addCertificate(MedicalCertificate newCert) {
        LOG.debug("Adding medical certificate = {}", newCert);
        service.persistMedicalCertificate(newCert);
        return Response.status(Response.Status.CREATED).entity(newCert).build();
    }

    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response updateCertificate(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, MedicalCertificate updatedCert) {
        LOG.debug("Updating certificate with id = {}", id);
        MedicalCertificate result = service.updateMedicalCertificate(id, updatedCert);
        if (result == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(result).build();
    }

    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteCertificate(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        LOG.debug("Deleting certificate with id = {}", id);
        MedicalCertificate deleted = service.deleteMedicalCertificate(id);
        if (deleted == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}