package escom.ttbackend.presentation.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientRecordResponse {
    private Long id;
    private int patientHeight;
    private int patientWeight;
    private LocalDateTime date;
}
