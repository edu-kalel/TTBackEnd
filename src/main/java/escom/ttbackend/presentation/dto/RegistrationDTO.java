package escom.ttbackend.presentation.dto;

import escom.ttbackend.model.enums.Ailment;
import escom.ttbackend.model.enums.Role;
import escom.ttbackend.model.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
//NO USAGES?????
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    private String email;
    private String clinic;
    private String first_name;
    private String last_name;
    private String password;
    private LocalDate date_of_birth;
    private String phone;
    private Sex sex;
    private Role role;
    private Set<Ailment> ailments;
    private String parent_email;
}
