package escom.ttbackend.model.entities;

import escom.ttbackend.model.converter.FractionConverter;
import escom.ttbackend.model.enums.DietOption;
import escom.ttbackend.model.enums.MealTime;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.Fraction;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meal")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "id_meal")
    private Long idMeal;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MealTime mealTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DietOption dietOption;
    @Convert(converter = FractionConverter.class)
    private Fraction quantity;
    private String unit;

    @ManyToOne
    @JoinColumn(name = "aliment", referencedColumnName = "id_aliment", nullable = false)
    private Aliment aliment;

    @ManyToOne
    @JoinColumn(name = "diet_plan", referencedColumnName = "id_diet_plan", nullable = false)
    private DietPlan dietPlan;


}
