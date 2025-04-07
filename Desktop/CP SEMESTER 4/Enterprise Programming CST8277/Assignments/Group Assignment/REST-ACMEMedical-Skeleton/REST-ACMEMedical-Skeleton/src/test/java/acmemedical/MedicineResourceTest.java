/**
 * File: MedicineResourceTest.java
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
import acmemedical.entity.Medicine;
import acmemedical.rest.resource.MedicineResource;

import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import java.util.List;

public class MedicineResourceTest {

    @InjectMocks
    private MedicineResource medicineResource;

    @Mock
    private ACMEMedicalService service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Logger logger;

    private Medicine medicine;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        medicine = new Medicine();
        medicine.setId(1);
        medicine.setDrugName("Aspirin");
    }

 // deleting a medicine
    @Test
    public void testDeleteMedicine() {
        when(service.deleteMedicine(1)).thenReturn(medicine);
        Response response = medicineResource.deleteMedicine(1);
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
    }

    

    @Test
    public void testDeleteMedicine_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false);
        Response response = medicineResource.deleteMedicine(1);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }
    
    //  getting all medicines
    @Test
    public void testGetAllMedicines() {
        List<Medicine> medicines = Arrays.asList(medicine);
        when(service.getAll(Medicine.class, "Medicine.findAll")).thenReturn(medicines);
        Response response = medicineResource.getAllMedicines();
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof List);
    }

    @Test
    public void testGetAllMedicines_ServiceError() {
        when(service.getAll(Medicine.class, "Medicine.findAll")).thenThrow(new RuntimeException("Service error"));
        Response response = medicineResource.getAllMedicines();
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    

    // adding a medicine
    @Test
    public void testAddMedicine() {
        when(service.persistMedicine(medicine)).thenReturn(medicine);
        Response response = medicineResource.addMedicine(medicine);
        assertEquals(CREATED.getStatusCode(), response.getStatus());
        assertEquals(medicine, response.getEntity());
    }

    @Test
    public void testAddMedicine_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = medicineResource.addMedicine(medicine);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }

    //updating a medicine
    @Test
    public void testUpdateMedicine() {
        when(service.updateMedicine(1, medicine)).thenReturn(medicine);
        Response response = medicineResource.updateMedicine(1, medicine);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(medicine, response.getEntity());
    }

    

    @Test
    public void testUpdateMedicine_Unauthorized() {
        when(securityContext.isCallerInRole("ADMIN")).thenReturn(false); 
        Response response = medicineResource.updateMedicine(1, medicine);
        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
    }
    
 // getting medicine by ID
    @Test
    public void testGetMedicineById() {
        when(service.getById(Medicine.class, "Medicine.findById", 1)).thenReturn(medicine);
        Response response = medicineResource.getMedicineById(1);
        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(medicine, response.getEntity());
    }

   

    
}
