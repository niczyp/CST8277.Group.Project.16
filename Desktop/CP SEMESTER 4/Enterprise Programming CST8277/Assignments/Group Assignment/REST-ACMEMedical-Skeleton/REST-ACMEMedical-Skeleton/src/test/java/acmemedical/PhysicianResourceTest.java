/**
 * File: PhysicianResourceTest.java
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.Physician;
import acmemedical.rest.resource.PhysicianResource;

import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import java.util.List;

public class PhysicianResourceTest {

    @InjectMocks
    private PhysicianResource physicianResource;

    @Mock
    private ACMEMedicalService service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Logger logger;

    private Physician physician;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        physician = new Physician();
        physician.setId(1);
        physician.setFirstName("Dr. John");
        physician.setLastName("Doe");
    }
    
  //deleting a physician
    @Test
    public void testDeletePhysician() {
        doNothing().when(service).deletePhysicianById(1);
        Response response = physicianResource.deletePhysician(1);
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
    }

    

    @Test
    public void testDeletePhysician_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = physicianResource.deletePhysician(1);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }

    // getting all physicians
    @Test
    public void testGetAllPhysicians() {
        List<Physician> physicians = Arrays.asList(physician);
        when(service.getAllPhysicians()).thenReturn(physicians);
        Response response = physicianResource.getPhysicians();
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof List);
    }

    @Test
    public void testGetAllPhysicians_ServiceError() {
        when(service.getAllPhysicians()).thenThrow(new RuntimeException("Service error"));
        Response response = physicianResource.getPhysicians();
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    

    //adding a physician
    @Test
    public void testAddPhysician() {
        when(service.persistPhysician(physician)).thenReturn(physician);
        Response response = physicianResource.addPhysician(physician);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(physician, response.getEntity());
    }

    @Test
    public void testAddPhysician_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = physicianResource.addPhysician(physician);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }

    // updating a physician
    @Test
    public void testUpdatePhysician() {
        when(service.updatePhysicianById(1, physician)).thenReturn(physician);
        Response response = physicianResource.updatePhysician(1, physician);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(physician, response.getEntity());
    }

    

    @Test
    public void testUpdatePhysician_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = physicianResource.updatePhysician(1, physician);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }
    
 // getting physician by ID
    @Test
    public void testGetPhysicianById() {
        when(service.getPhysicianById(1)).thenReturn(physician);
        Response response = physicianResource.getPhysicianById(1);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(physician, response.getEntity());
    }

   

    
}
