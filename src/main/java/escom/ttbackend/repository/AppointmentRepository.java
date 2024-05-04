package escom.ttbackend.repository;

import escom.ttbackend.model.compositekeys.AppointmentId;
import escom.ttbackend.model.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, AppointmentId> {
    List<Appointment> findByNutritionist_Email(String email);

    List<Appointment> findByPatient_Email(String email);
}
