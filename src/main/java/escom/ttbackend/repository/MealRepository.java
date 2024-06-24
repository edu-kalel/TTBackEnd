package escom.ttbackend.repository;

import escom.ttbackend.model.entities.Meal;
import escom.ttbackend.model.enums.DietOption;
import escom.ttbackend.model.enums.MealTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
//  List<Meal> findByAliment_IdAlimentAndMealTimeAndDietOption(Long aliment_idAliment, MealTime mealTime, DietOption dietOption);
  List<Meal> findByDietPlan_IdDietPlan(Long dietPlan_idDietPlan);

  List<Meal> findAllByDietPlan_IdDietPlanAndMealTimeAndDietOption(Long dietPlan_idDietPlan, MealTime mealTime, DietOption dietOption);
}