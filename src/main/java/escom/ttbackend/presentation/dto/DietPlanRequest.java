package escom.ttbackend.presentation.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import escom.ttbackend.model.entities.Meal;
import escom.ttbackend.model.enums.Goal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietPlanRequest {
    private String user_email;
    private Goal goal;
    private int kcal;
    private int patient_height;
    private int patient_weight;
    private LocalDate date_assigned;
    private String comment;
    @JsonBackReference
    private Set<Meal> meals;
}
