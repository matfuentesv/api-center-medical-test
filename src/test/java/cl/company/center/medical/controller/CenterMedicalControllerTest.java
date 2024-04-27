package cl.company.center.medical.controller;

import cl.company.center.medical.model.Patient;
import cl.company.center.medical.service.MedicalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CenterMedicalController.class)
@AutoConfigureMockMvc
 class CenterMedicalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalService medicalServiceMock;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void findAllPatientTest() throws Exception {
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient().setRun("19033397-1"));
        patients.add(new Patient().setRun("10.101.101-0"));

        when(medicalServiceMock.getAllPatient()).thenReturn(patients);

        String user = "H514228";
        String password = "admin";

        when(medicalServiceMock.findUser(user, password)).thenReturn(true);

        MockHttpServletRequestBuilder requestBuilder = get("/api/findAllPatient")
                .header("user", user)
                .header("password", password);


        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.patientList.length()").value(2))
                .andExpect(jsonPath("$._embedded.patientList[0].run").value("19033397-1"))
                .andExpect(jsonPath("$._embedded.patientList[1].run").value("10.101.101-0"));

        verify(medicalServiceMock).getAllPatient();
    }


    @Test
    void findPatientByRunTest() throws Exception {
        String user = "H514228";
        String password = "admin";
        String run = "19033397-1";
        Patient patient = new Patient().setRun(run).setNombre("Matias");

        when(medicalServiceMock.findUser(user, password)).thenReturn(true);

        MockHttpServletRequestBuilder requestBuilder = get("/api/findPatientByRun/{run}", run)
                .header("user", user)
                .header("password", password)
                .pathInfo("/api/findPatientByRun/" + run); // Agregar la ruta real con el valor de 'run'

        when(medicalServiceMock.findPatientByRun(run)).thenReturn(Optional.of(patient));

        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.run").value("19033397-1"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/findPatientByRun/" + run));

        verify(medicalServiceMock).findPatientByRun(run);
    }

    @Test
    void savePatient() throws Exception {
        Patient patient = new Patient()
                .setId(1L)
                .setNombre("Juan")
                .setApellido("Perez")
                .setEdad(30)
                .setDireccion("Calle Principal 123")
                .setCelular("123456789")
                .setRun("20.202.202-0");

        String user = "H514228";
        String password = "admin";
        when(medicalServiceMock.findUser(user, password)).thenReturn(true);

        when(medicalServiceMock.createPatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/api/createPatient")
                        .header("user", user)
                        .header("password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.edad").value(30))
                .andExpect(jsonPath("$.celular").value("123456789"));

        verify(medicalServiceMock).createPatient(any(Patient.class));
    }


    @Test
    void updatePatientTest() throws Exception {

        Patient patient = new Patient()
                .setId(1L)
                .setNombre("Juan")
                .setApellido("Perez")
                .setEdad(30)
                .setDireccion("Calle Principal 123")
                .setCelular("123456789")
                .setRun("20.202.202-0");

        String user = "H514228";
        String password = "admin";
        when(medicalServiceMock.findUser(user, password)).thenReturn(true);

        when(medicalServiceMock.updatePatient(any(Patient.class))).thenReturn(patient);
        mockMvc.perform(put("/api/updatePatient")
                        .header("user", user)
                        .header("password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk());

        verify(medicalServiceMock).updatePatient(any(Patient.class));
    }

}
