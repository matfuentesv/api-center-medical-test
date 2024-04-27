package cl.company.center.medical.service;

import cl.company.center.medical.model.Patient;
import cl.company.center.medical.repository.PatientRepository;
import cl.company.center.medical.service.impl.MedicalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @InjectMocks
    private MedicalServiceImpl medicalService;

    @Mock
    private PatientRepository patientRepositoryMock;


    @Test
    void findPatientTest() {
        Patient patient = new Patient()
                .setId(1L)
                .setNombre("Juan")
                .setApellido("Perez")
                .setEdad(30)
                .setDireccion("Calle Principal 123")
                .setCelular("123456789")
                .setRun("20.202.202-0");
        when(patientRepositoryMock.findByRun("20.202.202-0")).thenReturn(Optional.of(patient));
        Optional<Patient> foundPatient = medicalService.findPatientByRun("20.202.202-0");
        assertTrue(foundPatient.isPresent());
        assertEquals("Juan", foundPatient.get().getNombre());
    }

    @Test
    void findPatientAllTest() {

        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient().setRun("66.666.666-6"));
        patients.add(new Patient().setRun("19033396-1"));
        when(patientRepositoryMock.findAll()).thenReturn(patients);
        List<Patient> foundPatient = medicalService.getAllPatient();
        assertEquals(2, foundPatient.size());
    }

    @Test
    void saveCustomerTest() {

        Patient patient = new Patient()
                .setId(1L)
                .setNombre("Juan")
                .setApellido("Perez")
                .setEdad(30)
                .setDireccion("Calle Principal 123")
                .setCelular("123456789")
                .setRun("20.202.202-0");
        when(patientRepositoryMock.save(any())).thenReturn(patient);
        Patient savedPatient = medicalService.createPatient(patient);
        assertEquals("123456789", savedPatient.getCelular());
    }

    @Test
    void updateCustomerTest() {

        Patient patient = new Patient()
                .setId(1L)
                .setNombre("Juan")
                .setApellido("Perez")
                .setEdad(30)
                .setDireccion("Calle Principal 123")
                .setCelular("123456789")
                .setRun("20.202.202-0");

        when(patientRepositoryMock.save(any())).thenReturn(patient);

        Patient updatedPatient = medicalService.updatePatient(patient);
        assertEquals("123456789", updatedPatient.getCelular());
    }


    @Test
    void deletePatientTest() {

        medicalService.deletePatient(1L);
        verify(patientRepositoryMock, times(1)).deleteById(1L);
    }

}
