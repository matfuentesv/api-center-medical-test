package cl.company.center.medical.repository;

import cl.company.center.medical.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;


    @Test
    void testFindPatient() {
        Patient patient = new Patient()
                .setNombre("Juan")
                .setApellido("Perez")
                .setEdad(30)
                .setDireccion("Calle Principal 123")
                .setCelular("123456789")
                .setRun("20.202.202-0");
        Optional<Patient> findPatient = patientRepository.findByRun(patient.getRun());
        Assertions.assertTrue(findPatient.isPresent());
        assertEquals("20.202.202-0", findPatient.get().getRun());
    }

    @Test
    void testFindPatientAll() {

        Patient patient = new Patient()
                .setNombre("Juan")
                .setApellido("Perez")
                .setEdad(30)
                .setDireccion("Calle Principal 123")
                .setCelular("123456789")
                .setRun("6006939-1");

        patientRepository.save(patient);

        List<Patient> allPatient = patientRepository.findAll();
        Assertions.assertTrue(allPatient.stream().anyMatch(x -> x.getRun().equalsIgnoreCase("22.222.222-2")));
        Assertions.assertTrue(allPatient.stream().anyMatch(x -> x.getRun().equalsIgnoreCase("99.999.999-9")));
        Assertions.assertTrue(allPatient.stream().anyMatch(x -> x.getRun().equalsIgnoreCase("19033397-1")));
    }


    @Test
    void savePatient() {

        Patient patient = new Patient()
                .setNombre("Juan")
                .setApellido("Perez")
                .setEdad(30)
                .setDireccion("Calle Principal 123")
                .setCelular("123456789")
                .setRun("6006939-6");
        final Patient p = patientRepository.save(patient);
        assertNotNull(p.getRun());
        assertEquals("6006939-6", p.getRun());
    }


    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void updatePatient() {

        Patient patient = new Patient()
                .setNombre("Juan")
                .setApellido("Perez")
                .setEdad(30)
                .setDireccion("Calle Principal 123")
                .setCelular("123456789")
                .setRun("6006939-6");


        Patient savedPatient = patientRepository.save(patient);

        savedPatient.setNombre("Matias");
        savedPatient.setApellido("Perez");
        savedPatient.setEdad(30);
        savedPatient.setDireccion("Calle Principal 9131");
        savedPatient.setCelular("123456789");
        savedPatient.setRun("6006939-6");

        Patient updatedPatient = patientRepository.save(savedPatient);
        assertNotNull(updatedPatient.getId());
        assertEquals("Matias", updatedPatient.getNombre());
        assertEquals(30, updatedPatient.getEdad());
    }


    @Test
    void deletePatient() {

        Patient patient = new Patient()
                .setNombre("Juan")
                .setApellido("Perez")
                .setEdad(30)
                .setDireccion("Calle Principal 123")
                .setCelular("123456789")
                .setRun("6006939-6");


        Patient savedPatient = patientRepository.save(patient);

        savedPatient.setNombre("Matias");
        savedPatient.setApellido("Perez");
        savedPatient.setEdad(30);
        savedPatient.setDireccion("Calle Principal 9131");
        savedPatient.setCelular("123456789");
        savedPatient.setRun("6006939-6");

        Patient savePatient = patientRepository.save(savedPatient);


        assertTrue(patientRepository.findById(savePatient.getId()).isPresent());


        patientRepository.deleteById(savedPatient.getId());


        Optional<Patient> deletedPatient = patientRepository.findById(savedPatient.getId());
        assertFalse(deletedPatient.isPresent());
    }


}
