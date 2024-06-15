package escom.ttbackend.presentation.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppointmentRequestFull {
  private String nutritionist_email;
  private String patient_email;
  private LocalDateTime starting_time;
  private LocalDateTime ending_time;
}