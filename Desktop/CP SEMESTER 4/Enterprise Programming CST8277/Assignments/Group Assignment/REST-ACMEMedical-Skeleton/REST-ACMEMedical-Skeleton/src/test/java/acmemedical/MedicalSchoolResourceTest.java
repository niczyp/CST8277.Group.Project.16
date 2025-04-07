/**
 * File: MedicalSchoolResourceTest.java
 * Author(s): Prince Khakhriya
 * Date Modified: 2025-04-05
 * Due Date: 2025-04-06
 */
package acmemedical;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static jakarta.ws.rs.core.Response.Status.*;

import jakarta.ws.rs.core.Response;
import jakarta.security.enterprise.SecurityContext;
import jakarta.validation.constraints.AssertFalse.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalSchool;
import acmemedical.entity.MedicalTraining;
import acmemedical.rest.resource.MedicalSchoolResource;

import java.util.Arrays;
import java.util.Optional;

public class MedicalSchoolResourceTest {

    @InjectMocks
    private MedicalSchoolResource medicalSchoolResource;

    @Mock
    private ACMEMedicalService service;

    @Mock
    private SecurityContext securityContext;

    private MedicalSchool medicalSchool;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
       // medicalSchool = new MedicalSchool();
        medicalSchool.setId(1);
        medicalSchool.setName("Harvard Medical School");
    }

    

    // getting a medical school by ID
    @Test
    public void testGetMedicalSchoolById() {
        when(service.getMedicalSchoolById(1)).thenReturn(medicalSchool);
        Response response = medicalSchoolResource.getMedicalSchoolById(1);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(medicalSchool, response.getEntity());
    }

    

    

    //deleting a medical school
    @Test
    public void testDeleteMedicalSchool() {
        when(service.deleteMedicalSchool(1)).thenReturn(medicalSchool);
        Response response = medicalSchoolResource.deleteMedicalSchool(1);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(medicalSchool, response.getEntity());
    }

    

    @Test
    public void testDeleteMedicalSchool_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = medicalSchoolResource.deleteMedicalSchool(1);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }

    //updating a medical school
    @Test
    public void testUpdateMedicalSchool() {
        when(service.updateMedicalSchool(1, medicalSchool)).thenReturn(medicalSchool);
        Response response = medicalSchoolResource.updateMedicalSchool(1, medicalSchool);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(medicalSchool, response.getEntity());
    }

    

    @Test
    public void testUpdateMedicalSchool_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = medicalSchoolResource.updateMedicalSchool(1, medicalSchool);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }
    
 // adding a medical school
    @Test
    public void testAddMedicalSchool() {
        when(service.isDuplicated(medicalSchool)).thenReturn(false);
        when(service.persistMedicalSchool(medicalSchool)).thenReturn(medicalSchool);
        Response response = medicalSchoolResource.addMedicalSchool(medicalSchool);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(medicalSchool, response.getEntity());
    }

    @Test
    public void testAddMedicalSchool_Conflict() {
        when(service.isDuplicated(medicalSchool)).thenReturn(true);
        Response response = medicalSchoolResource.addMedicalSchool(medicalSchool);
        assertEquals(CONFLICT.getStatusCode(), response.getStatus());
    }

    //adding medical training to a medical school
    @Test
    public void testAddMedicalTrainingToMedicalSchool() {
        MedicalTraining medicalTraining = new MedicalTraining();
        when(service.getMedicalSchoolById(1)).thenReturn(medicalSchool);
        when(service.updateMedicalSchool(1, medicalSchool)).thenReturn(medicalSchool);
        Response response = medicalSchoolResource.addMedicalTrainingToMedicalSchool(1, medicalTraining);
        assertEquals(OK.getStatusCode(), response.getStatus());
    }

    

    @Test
    public void testAddMedicalTrainingToMedicalSchool_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        MedicalTraining medicalTraining = new MedicalTraining();
        Response response = medicalSchoolResource.addMedicalTrainingToMedicalSchool(1, medicalTraining);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }
    
 // getting all medical schools
    @Test
    public void testGetMedicalSchools() {
        when(service.getAllMedicalSchools()).thenReturn(Arrays.asList(medicalSchool));
        Response response = medicalSchoolResource.getMedicalSchools();
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof List);
    }

    @Test
    public void testGetMedicalSchools_ServiceError() {
        when(service.getAllMedicalSchools()).thenThrow(new RuntimeException("Service error"));
        Response response = medicalSchoolResource.getMedicalSchools();
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }
}
