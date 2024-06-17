package escom.ttbackend.presentation.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietPlanDTO {
    private Long id;
    private String patientEmail;
    private String goal;
    private LocalDate date;
    private String comment;
    private String breakfast1;
    private String colation1_1;
    private String lunch1;
    private String colation2_1;
    private String dinner1;
    private String breakfast2;
    private String colation1_2;
    private String lunch2;
    private String colation2_2;
    private String dinner2;
}
