package escom.ttbackend.presentation.dto;

import escom.ttbackend.model.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SimpleAppointmentDTO {
    private Long id_appointment;
    private String patient;
    private LocalDateTime starting_time;
}
