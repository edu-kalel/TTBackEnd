package escom.ttbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import escom.ttbackend.model.enums.Goal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "diet_plan")
public class DietPlan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id_diet_plan;
    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
    private User user_email;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Goal goal;
    private int patient_height;
    private int patient_weight;
    @Column(nullable = false)
    private LocalDate date_assigned;
    private String comment;
    @OneToMany(mappedBy = "id_diet_plan")
    @JsonBackReference
    private Set<Meal> meals;
}
