package escom.ttbackend.repository;

import escom.ttbackend.model.entities.PatientRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRecordRepository extends JpaRepository<PatientRecord, Long> {
    List<PatientRecord> findByPatient_EmailOrderByDateDesc(String patientEmail);

    PatientRecord findFirstByPatient_EmailOrderByDateDesc(String patientEmail);

    List<PatientRecord> findByPatient_Email(String patientEmail);
}
