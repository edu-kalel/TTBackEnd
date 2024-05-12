package escom.ttbackend.repository;

import escom.ttbackend.model.entities.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    List<DietPlan> findByUser_Email(String email);

    DietPlan findFirstByUser_EmailOrderByDateDesc(String patientEmail);
}
