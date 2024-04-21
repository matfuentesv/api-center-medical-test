package cl.company.center.medical.service;

import cl.company.center.medical.model.Doctor;
import cl.company.center.medical.model.HistoricalMedical;
import cl.company.center.medical.model.Patient;
import cl.company.center.medical.model.User;

import java.util.List;
import java.util.Optional;

public interface MedicalService {

    Optional<User> findUser(String user);
    List<HistoricalMedical> getHistoryPatientByRun(String run);
    List<Doctor> getAllDoctor();
    Optional<Doctor> findDoctorByRun(String run);
    Doctor createDoctor(Doctor doctor);
    Doctor updateDoctor(Doctor doctor);
    void deleteDoctor(Long id);
    boolean existsDoctorById(Long id);
    List<Patient> getAllPatient();
    Optional<Patient> findPatientByRun(String run);
    Patient createPatient(Patient patient);
    Patient updatePatient(Patient patient);
    boolean existsPatientById(Long id);
    void deletePatient(Long id);

}
