package cl.company.center.medical.service;

import cl.company.center.medical.model.Doctor;
import cl.company.center.medical.model.Patient;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    ResponseEntity<Object>findUser(String user,String password);
    ResponseEntity<Object>findAllDoctor(String user,String password);
    ResponseEntity<Object>findDoctor(String user,String password,String name);
    ResponseEntity<Object>getHistoryPatientByRun(String user,String password,String name);
    ResponseEntity<Object> createDoctor(String user,String password,Doctor doctor);
    ResponseEntity<Object> updateDoctor(String user,String password,Long id,Doctor doctor);
    ResponseEntity<Object> deleteDoctor(String user,String password,Long id);
    ResponseEntity<Object>findAllPatient(String user,String password);
    ResponseEntity<Object>findPatientByRun(String user,String password,String run);
    ResponseEntity<Object> createPatient(String user, String password, Patient patient);
    ResponseEntity<Object> updatePatient(String user,String password,Long id,Patient patient);
    ResponseEntity<Object> deletePatient(String user,String password,Long id);

}
