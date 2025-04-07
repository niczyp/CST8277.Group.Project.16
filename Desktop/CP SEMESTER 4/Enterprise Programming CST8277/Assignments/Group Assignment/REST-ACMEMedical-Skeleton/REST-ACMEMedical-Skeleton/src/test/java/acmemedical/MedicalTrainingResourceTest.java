package acmemedical;
/**
 * File: MedicalTrainingResourceTest.java
 * Author(s): Prince Khakhriya
 * Date Modified: 2025-04-05
 * Due Date: 2025-04-06
 */
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static jakarta.ws.rs.core.Response.Status.*;

import jakarta.ws.rs.core.Response;
import jakarta.security.enterprise.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalTraining;
import acmemedical.rest.resource.MedicalTrainingResource;
import antlr.collections.List;

import java.util.Arrays;
import java.util.Optional;

public class MedicalTrainingResourceTest {

    @InjectMocks
    private MedicalTrainingResource medicalTrainingResource;

    @Mock
    private ACMEMedicalService service;

    @Mock
    private SecurityContext securityContext;

    private MedicalTraining medicalTraining;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        medicalTraining = new MedicalTraining();
        medicalTraining.setId(1);
        //medicalTraining.setName("Basic Life Support");
    }

    

    // getting medical training by ID
    @Test
    public void testGetTrainingById() {
        when(service.getById(MedicalTraining.class, MedicalTraining.FIND_BY_ID, 1))
                .thenReturn(medicalTraining);
        Response response = medicalTrainingResource.getTrainingById(1);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(medicalTraining, response.getEntity());
    }

    

    

    //updating a medical training
    @Test
    public void testUpdateTraining() {
        when(service.updateMedicalTraining(1, medicalTraining)).thenReturn(medicalTraining);
        Response response = medicalTrainingResource.updateTraining(1, medicalTraining);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(medicalTraining, response.getEntity());
    }

    

    @Test
    public void testUpdateTraining_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = medicalTrainingResource.updateTraining(1, medicalTraining);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }
    
  //adding a medical training
    @Test
    public void testAddMedicalTraining() {
        when(service.persistMedicalTraining(medicalTraining)).thenReturn(medicalTraining);
        Response response = medicalTrainingResource.addMedicalTraining(medicalTraining);
        assertEquals(CREATED.getStatusCode(), response.getStatus());
        assertEquals(medicalTraining, response.getEntity());
    }

    @Test
    public void testAddMedicalTraining_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); // Simulate unauthorized user
        Response response = medicalTrainingResource.addMedicalTraining(medicalTraining);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }

    // deleting a medical training
    @Test
    public void testDeleteTraining() {
        when(service.deleteMedicalTraining(1)).thenReturn(medicalTraining);
        Response response = medicalTrainingResource.deleteTraining(1);
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
    }

    

    @Test
    public void testDeleteTraining_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false);
        Response response = medicalTrainingResource.deleteTraining(1);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }
    
 // getting all medical trainings
    @Test
    public void testGetAllMedicalTrainings() {
        when(service.getAll(MedicalTraining.class, MedicalTraining.ALL_TRAINING_QUERY_NAME))
                .thenReturn(Arrays.asList(medicalTraining));
        Response response = medicalTrainingResource.getAllMedicalTrainings();
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof List);
    }

    @Test
    public void testGetAllMedicalTrainings_ServiceError() {
        when(service.getAll(MedicalTraining.class, MedicalTraining.ALL_TRAINING_QUERY_NAME))
                .thenThrow(new RuntimeException("Service error"));
        Response response = medicalTrainingResource.getAllMedicalTrainings();
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }
}
