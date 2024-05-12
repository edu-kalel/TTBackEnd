package escom.ttbackend.model.entities;

import escom.ttbackend.model.enums.MealTime;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Meal {
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    @Enumerated(EnumType.STRING)
    private MealTime mealTime;
    private String aliment;
}
