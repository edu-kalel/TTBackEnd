package escom.ttbackend.repository;

import escom.ttbackend.model.compositekeys.AppointmentId;
import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByNutritionist_Email(String email);

    List<Appointment> findByPatient_Email(String email);

    List<Appointment> findByNutritionist_EmailAndAppointmentStatus(String email, AppointmentStatus status);

//    @Query("SELECT a FROM Appointment a WHERE a.nutritionist = :nutritionist " +
//            "AND ((a.starting_time BETWEEN :startTime AND :endTime) OR " +
//            "(a.ending_time BETWEEN :startTime AND :endTime))")
//    List<Appointment> findByNutritionistAndTimeOverlap(@Param("nutritionist") User nutritionist,
//                                                       @Param("startTime") LocalDateTime startTime,
//                                                       @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Appointment a WHERE a.nutritionist = :nutritionist " +
            "AND ((a.starting_time BETWEEN :startTime AND :endTime) OR " +
            "(a.ending_time BETWEEN :startTime AND :endTime)) " +
            "AND a.appointmentStatus = :status")
    List<Appointment> findByNutritionistAndTimeOverlapWithStatus(
            @Param("nutritionist") User nutritionist,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") AppointmentStatus status);

}
