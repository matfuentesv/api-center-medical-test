package cl.company.center.medical.repository;


import cl.company.center.medical.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    @Query("SELECT u FROM Paciente u WHERE u.run = ?1")
    Optional<Patient> findByRun(String run);
}
