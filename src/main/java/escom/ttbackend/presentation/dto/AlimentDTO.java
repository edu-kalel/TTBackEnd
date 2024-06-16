package escom.ttbackend.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlimentDTO {
  private String groupName;
  private String name;
  private String unit;
  private int whole;
  private int numerator;
  private int denominator;
}
