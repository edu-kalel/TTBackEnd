package escom.ttbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import escom.ttbackend.model.enums.MealTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meal")
public class Meal implements Serializable {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id_meal;
    @ManyToOne
    @JoinColumn(name = "id_diet_plan", referencedColumnName = "id_diet_plan", nullable = false)
    private DietPlan id_diet_plan;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;    //TODO: maybe implement own enum of weekdays with translations ?
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MealTime meal_time;
    @OneToMany(mappedBy = "meal")
    @JsonBackReference
    private Set<AlimentPortion> alimentPortionSet;
}
