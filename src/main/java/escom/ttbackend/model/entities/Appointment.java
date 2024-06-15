package escom.ttbackend.model.entities;

import escom.ttbackend.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointment")
//@IdClass(AppointmentId.class)
public class Appointment implements Serializable {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "id_appointment")
    private Long idAppointment;
//    @Id
    @ManyToOne
    @JoinColumn(name = "nutritionist", referencedColumnName = "email", nullable = false)
    private User nutritionist;
//    @Id
    @Column(name = "starting_time")
    private LocalDateTime startingTime;
    @ManyToOne
    @JoinColumn(name = "patient", referencedColumnName = "email", nullable = false)
    private User patient;
    @Column(nullable = false, name = "ending_time")
    private LocalDateTime endingTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus appointmentStatus;
}
