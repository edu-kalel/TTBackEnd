package escom.ttbackend.presentation.dto;

import escom.ttbackend.model.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppointmentDTO {
    private Long id_appointment;
    private String nutritionist;
    private LocalDateTime starting_time;
    private String patient;
    private LocalDateTime ending_time;
    private AppointmentStatus appointmentStatus;
}
