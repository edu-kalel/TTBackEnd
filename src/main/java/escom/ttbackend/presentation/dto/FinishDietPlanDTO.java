package escom.ttbackend.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinishDietPlanDTO {
  private Long id;
  private String goal;
  private String comment;
}
