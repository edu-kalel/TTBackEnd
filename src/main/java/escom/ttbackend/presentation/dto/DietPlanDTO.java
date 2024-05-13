package escom.ttbackend.presentation.dto;

import escom.ttbackend.model.entities.Meal;
import escom.ttbackend.model.enums.Goal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietPlanDTO {
    private String user_email;
    private Goal goal;
    private int kcal;
    private String comment;
    private Set<Meal> meals;
}
