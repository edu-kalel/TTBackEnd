package escom.ttbackend.presentation.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleDietPlanDTO {
  private Long id;
  private LocalDate date;
  private String goal;
}
