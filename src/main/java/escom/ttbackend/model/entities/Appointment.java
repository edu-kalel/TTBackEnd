package escom.ttbackend.model.entities;

import escom.ttbackend.model.compositekeys.AppointmentId;
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
    @Column(nullable = false, updatable = false)
    private Long id_appointment;
//    @Id
    @ManyToOne
    @JoinColumn(name = "nutritionist", referencedColumnName = "email", nullable = false)
    private User nutritionist;
//    @Id
    private LocalDateTime starting_time;
    @ManyToOne
    @JoinColumn(name = "patient", referencedColumnName = "email", nullable = false)
    private User patient;
    @Column(nullable = false)
    private LocalDateTime ending_time;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus appointmentStatus;
}
