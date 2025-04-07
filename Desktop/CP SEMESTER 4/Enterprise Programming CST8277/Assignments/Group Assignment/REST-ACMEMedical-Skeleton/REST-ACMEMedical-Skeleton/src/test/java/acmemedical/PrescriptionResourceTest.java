package acmemedical;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static jakarta.ws.rs.core.Response.Status.*;

import jakarta.ws.rs.core.Response;
import jakarta.security.enterprise.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.Prescription;
import acmemedical.entity.PrescriptionPK;
import acmemedical.rest.resource.PrescriptionResource;

import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import java.util.List;

public class PrescriptionResourceTest {

    @InjectMocks
    private PrescriptionResource prescriptionResource;

    @Mock
    private ACMEMedicalService service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Logger logger;

    private Prescription prescription;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        prescription = new Prescription();
        PrescriptionPK pk = new PrescriptionPK(1, 30); 
        prescription.setId(pk);
       
    }
    
 // deleting a prescription
    @Test
    public void testDeletePrescription() {
        PrescriptionPK pk = new PrescriptionPK(1, 30); 
        when(service.deletePrescription(pk)).thenReturn(prescription);
        Response response = prescriptionResource.deletePrescription(1, 30);
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
    }

   

    @Test
    public void testDeletePrescription_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = prescriptionResource.deletePrescription(1, 30);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }

    // getting all prescriptions
    @Test
    public void testGetAllPrescriptions() {
        List<Prescription> prescriptions = Arrays.asList(prescription);
        when(service.getAll(Prescription.class, "Prescription.findAll")).thenReturn(prescriptions);
        Response response = prescriptionResource.getAllPrescriptions();
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof List);
    }

    @Test
    public void testGetAllPrescriptions_ServiceError() {
        when(service.getAll(Prescription.class, "Prescription.findAll")).thenThrow(new RuntimeException("Service error"));
        Response response = prescriptionResource.getAllPrescriptions();
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    

    // adding a prescription
    @Test
    public void testAddPrescription() {
        when(service.persistPrescription(prescription)).thenReturn(prescription);
        Response response = prescriptionResource.addPrescription(prescription);
        assertEquals(CREATED.getStatusCode(), response.getStatus());
        assertEquals(prescription, response.getEntity());
    }

    @Test
    public void testAddPrescription_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = prescriptionResource.addPrescription(prescription);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }

    // updating a prescription
    @Test
    public void testUpdatePrescription() {
        PrescriptionPK pk = new PrescriptionPK(1, 30); // example IDs
        when(service.updatePrescription(pk, prescription)).thenReturn(prescription);
        Response response = prescriptionResource.updatePrescription(1, 30, prescription);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(prescription, response.getEntity());
    }

   

    @Test
    public void testUpdatePrescription_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = prescriptionResource.updatePrescription(1, 30, prescription);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }
    
 // getting a prescription by composite key
    @Test
    public void testGetPrescriptionById() {
        PrescriptionPK pk = new PrescriptionPK(1, 30); 
        when(service.getPrescriptionByPK(pk)).thenReturn(prescription);
        Response response = prescriptionResource.getPrescriptionById(1, 30);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(prescription, response.getEntity());
    }

    

    
}
