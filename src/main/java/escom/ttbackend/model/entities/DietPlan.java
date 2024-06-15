package escom.ttbackend.model.entities;

import escom.ttbackend.model.converter.FractionConverter;
import escom.ttbackend.model.enums.Goal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.Fraction;

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
    @JoinColumn(name = "user", referencedColumnName = "email", nullable = false)
    private User user;
    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
    private String goal;
    @Column(nullable = false)
    private int kcal;
    @Column(nullable = false)
    private LocalDate date;
    private String comment;
    @ElementCollection
    @CollectionTable(
            name="meal",
            joinColumns = @JoinColumn(name = "diet_plan")
    )
    private Set<Meal> meals;
    @Convert(converter = FractionConverter.class)
    private Fraction fraction;
}


//REMEMBEEEEER, PATIENT DOESN'T NEED ALL HISTORIAL OF DIET_PLANS, JUST WITH THE LAST ONE IS ENOOOOOUUUUGH :D