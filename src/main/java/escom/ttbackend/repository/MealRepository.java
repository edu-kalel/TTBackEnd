package escom.ttbackend.repository;

import escom.ttbackend.model.entities.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {

}