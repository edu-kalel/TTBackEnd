package escom.ttbackend.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlimentDTO {
  private Long id;
  private String name;
  private String group;
  private String quantity;
  private String unit;
  private String kcal;
  private String carbs;
  private String fats;
  private String proteins;
}
