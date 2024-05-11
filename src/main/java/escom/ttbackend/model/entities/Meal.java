package escom.ttbackend.model.entities;

import escom.ttbackend.model.enums.MealTime;
import jakarta.persistence.Embeddable;
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
    private DayOfWeek dayOfWeek;
    private MealTime mealTime;
//    private int quantity;
    private String aliment;
}
