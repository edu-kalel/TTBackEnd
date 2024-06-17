package escom.ttbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(nullable = false, updatable = false, name = "id_diet_plan")
    private Long idDietPlan;
    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "email", nullable = false)
    private User user;
//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
    private String goal;
//    @Column(nullable = false)
//    private int kcal;
//    @Column(nullable = true)
    private LocalDate date;
    private String comment;
    @OneToMany(mappedBy = "dietPlan", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Meal> meals;
}


//REMEMBEEEEER, PATIENT DOESN'T NEED ALL HISTORIAL OF DIET_PLANS, JUST WITH THE LAST ONE IS ENOOOOOUUUUGH :D