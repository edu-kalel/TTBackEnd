package escom.ttbackend.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlimentGroupDTO {
  private String name;
  private int kcal;
  private int carbs;
  private int fats;
  private int proteins;
}
