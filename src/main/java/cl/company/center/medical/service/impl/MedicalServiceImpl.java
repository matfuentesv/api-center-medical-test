package cl.company.center.medical.service.impl;

import cl.company.center.medical.model.Doctor;
import cl.company.center.medical.model.HistoricalMedical;
import cl.company.center.medical.model.Patient;
import cl.company.center.medical.model.User;
import cl.company.center.medical.repository.DoctorRepository;
import cl.company.center.medical.repository.MedicalRecordRepository;
import cl.company.center.medical.repository.PatientRepository;
import cl.company.center.medical.repository.UserRepository;
import cl.company.center.medical.service.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
public class MedicalServiceImpl implements MedicalService {


    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Optional<User> findUser(String user) {
        return userRepository.findUser(user);
    }

    @Override
    public List<HistoricalMedical> getHistoryPatientByRun(String run) {
        Predicate<HistoricalMedical> predicate = x-> x.getPaciente().getRun().equalsIgnoreCase(run);
        return medicalRecordRepository
                               .findAll()
                               .stream()
                               .filter(predicate)
                               .collect(Collectors.toList());
    }

    @Override
    public List<Doctor> getAllDoctor() {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> findDoctorByRun(String name) {
        return doctorRepository.findByRun(name);
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public boolean existsDoctorById(Long id) {
        return doctorRepository.existsById(id);
    }

    @Override
    public List<Patient> getAllPatient() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findPatientByRun(String run) {
        return patientRepository.findByRun(run);
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public boolean existsPatientById(Long id) {
        return patientRepository.existsById(id);
    }

    @Override
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
