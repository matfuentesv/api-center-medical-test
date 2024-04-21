package cl.company.center.medical.repository;


import cl.company.center.medical.model.HistoricalMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<HistoricalMedical,Long> {

    @Query("SELECT u FROM Historial_Medico u WHERE u.doctor.id = ?1")
    Optional<HistoricalMedical> findDoctor(Long id);

    @Query("SELECT u FROM Historial_Medico u WHERE u.paciente.id = ?1")
    Optional<HistoricalMedical> findPatient(Long id);


}
