/**
 * File: MedicalCertiResourceTest.java
 * Author(s): Prince Khakhriya
 * Date Modified: 2025-04-05
 * Due Date: 2025-04-06
 */
package acmemedical;

import acmemedical.ejb.ACMEMedicalService;
import acmemedical.entity.MedicalCertificate;
import acmemedical.rest.resource.MedicalCertificateResource;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalCertiResourceTest {

    @InjectMocks
    private MedicalCertificateResource resource;

    @Mock
    private ACMEMedicalService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    

    @Test
    void testGetCertificateById_found() {
        MedicalCertificate mockCert = new MedicalCertificate();
        when(service.getById(MedicalCertificate.class, "MedicalCertificate.findById", 1)).thenReturn(mockCert);

        Response response = resource.getCertificateById(1);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(mockCert, response.getEntity());
    }

    

    @Test
    void testAddCertificate() {
        MedicalCertificate newCert = new MedicalCertificate();

        Response response = resource.addCertificate(newCert);

        verify(service).persistMedicalCertificate(newCert);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(newCert, response.getEntity());
    }

    @Test
    void testUpdateCertificate_found() {
        MedicalCertificate updatedCert = new MedicalCertificate();
        when(service.updateMedicalCertificate(1, updatedCert)).thenReturn(updatedCert);

        Response response = resource.updateCertificate(1, updatedCert);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(updatedCert, response.getEntity());
    }

    @Test
    void testUpdateCertificate_notFound() {
        MedicalCertificate updatedCert = new MedicalCertificate();
        when(service.updateMedicalCertificate(1, updatedCert)).thenReturn(null);

        Response response = resource.updateCertificate(1, updatedCert);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void testDeleteCertificate_found() {
        MedicalCertificate deletedCert = new MedicalCertificate();
        when(service.deleteMedicalCertificate(1)).thenReturn(deletedCert);

        Response response = resource.deleteCertificate(1);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
    
    @Test
    void testGetAllCertificates() {
        List<MedicalCertificate> mockCerts = Arrays.asList(new MedicalCertificate(), new MedicalCertificate());
        when(service.getAll(MedicalCertificate.class, "MedicalCertificate.findAll")).thenReturn(mockCerts);

        Response response = resource.getAllCertificates();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(mockCerts, response.getEntity());
    }

   
}
