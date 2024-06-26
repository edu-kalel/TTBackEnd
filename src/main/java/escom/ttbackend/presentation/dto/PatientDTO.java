package escom.ttbackend.presentation.dto;

import escom.ttbackend.model.enums.Ailment;
import escom.ttbackend.model.enums.Role;
import escom.ttbackend.model.enums.Sex;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PatientDTO {
    private String email;
    private String clinic;
    private String first_name;
    private String last_name;
    private LocalDate date_of_birth;
    private String phone;
    private Sex sex;
    private Role role;
    private String parent_email;
    private Set<Ailment> ailments;
}
