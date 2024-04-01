package escom.ttbackend.model.entities;

import escom.ttbackend.model.compositekeys.AppointmentId;
import escom.ttbackend.model.compositekeys.HierarchyId;
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
@IdClass(AppointmentId.class)
public class Appointment implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "nutritionist_email", referencedColumnName = "email", nullable = false)
    private User nutritionist_email;
    @Id
    private LocalDateTime starting_time;
    @ManyToOne
    @JoinColumn(name = "patient_email", referencedColumnName = "email", nullable = false)
    private User patient_email;
    @Column(nullable = false)
    private LocalDateTime finishing_time;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus appointmentStatus;
}
