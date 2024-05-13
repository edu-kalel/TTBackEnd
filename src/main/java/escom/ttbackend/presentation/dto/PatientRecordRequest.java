package escom.ttbackend.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientRecordRequest {
    private String patientEmail;
    private int patientHeight;
    private int patientWeight;
    private String comment;
}
