package escom.ttbackend.repository;

import escom.ttbackend.model.entities.PatientRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRecordRepository extends JpaRepository<PatientRecord, Long> {
    List<PatientRecord> findByPatient_EmailOrderByDateDesc(String patientEmail);
}
