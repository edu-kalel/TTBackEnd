package escom.ttbackend.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IndividualFoodDTO {
  private Long alimentId;
  private Double quantity;
}
