package escom.ttbackend.presentation.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleAppointmentDTO {
    private Long id_appointment;
    private String patient;
    private LocalDateTime starting_time;
}
