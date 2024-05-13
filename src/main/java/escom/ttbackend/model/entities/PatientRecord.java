package escom.ttbackend.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient_record")
public class PatientRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "id_patient_record")
    private Long idPatientRecord;
    @ManyToOne
    @JoinColumn(name = "patient", referencedColumnName = "email", nullable = false)
    private User patient;
    @Column(nullable = false, updatable = false, name = "patient_height")
    private int patientHeight;
    @Column(nullable = false, updatable = false, name = "patient_weight")
    private int patientWeight;
    private String comment;
    @Column(nullable = false, updatable = false)
    private LocalDate date;
}
