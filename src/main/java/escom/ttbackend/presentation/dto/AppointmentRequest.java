package escom.ttbackend.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppointmentRequest {
    private String patient_email;
    private LocalDateTime starting_time;
    private LocalDateTime ending_time;
}
