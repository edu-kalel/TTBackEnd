package escom.ttbackend.presentation.dto;

import escom.ttbackend.model.enums.DietOption;
import escom.ttbackend.model.enums.MealTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MealsToAddDTO {
  private Long dietPlanId;
  private MealTime mealTime;
  private DietOption dietOption;
  private Set<IndividualFoodDTO> meals;
}
