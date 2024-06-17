package escom.ttbackend.repository;

import escom.ttbackend.model.entities.DietPlan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    DietPlan findByUser_Email(String email);

    List<DietPlan> findAllByUser_EmailOrderByDateDesc(String email);

    DietPlan findFirstByUser_EmailOrderByDateDesc(String patientEmail);
}
