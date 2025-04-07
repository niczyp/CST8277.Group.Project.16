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
import acmemedical.entity.Patient;
import acmemedical.rest.resource.PatientResource;

import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import java.util.List;

public class PatientResourceTest {

    @InjectMocks
    private PatientResource patientResource;

    @Mock
    private ACMEMedicalService service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Logger logger;

    private Patient patient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        patient = new Patient();
        patient.setId(1);
        patient.setFirstName("John Doe");
        patient.setId(30);
    }
    
 // deleting a patient
    @Test
    public void testDeletePatient() {
        when(service.deletePatient(1)).thenReturn(patient);
        Response response = patientResource.deletePatient(1);
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
    }

    

    @Test
    public void testDeletePatient_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = patientResource.deletePatient(1);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }

    

    //getting patient by ID
    @Test
    public void testGetPatientById() {
        when(service.getById(Patient.class, "Patient.findById", 1)).thenReturn(patient);
        Response response = patientResource.getPatientById(1);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(patient, response.getEntity());
    }

    

    //adding a patient
    @Test
    public void testAddPatient() {
        when(service.persistPatient(patient)).thenReturn(patient);
        Response response = patientResource.addPatient(patient);
        assertEquals(CREATED.getStatusCode(), response.getStatus());
        assertEquals(patient, response.getEntity());
    }

    @Test
    public void testAddPatient_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = patientResource.addPatient(patient);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }

    //updating a patient
    @Test
    public void testUpdatePatient() {
        when(service.updatePatient(1, patient)).thenReturn(patient);
        Response response = patientResource.updatePatient(1, patient);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(patient, response.getEntity());
    }

    

    @Test
    public void testUpdatePatient_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = patientResource.updatePatient(1, patient);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }
    
 // getting all patients
    @Test
    public void testGetAllPatients() {
        List<Patient> patients = Arrays.asList(patient);
        when(service.getAll(Patient.class, "Patient.findAll")).thenReturn(patients);
        Response response = patientResource.getAllPatients();
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof List);
    }

    @Test
    public void testGetAllPatients_ServiceError() {
        when(service.getAll(Patient.class, "Patient.findAll")).thenThrow(new RuntimeException("Service error"));
        Response response = patientResource.getAllPatients();
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    
}
