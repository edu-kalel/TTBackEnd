package escom.ttbackend.presentation.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BigPatientInfoDTO {
    private String email;
    private String fullName;
    private int age;
    private int weight;
    private int height;
}
