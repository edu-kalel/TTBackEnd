package escom.ttbackend.presentation.dto;

import escom.ttbackend.model.enums.ActivityLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientRecordRequest {
    private String patientEmail;
    private int patientHeight;
    private int patientWeight;
    private String comment;
    private ActivityLevel activityLevel;
}
