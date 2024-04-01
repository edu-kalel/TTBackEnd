package escom.ttbackend.config;

import escom.ttbackend.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
        private String email;
        private String first_name;
        private String last_name;
        private String password;
        private LocalDate date_of_birth;
        private String phone;
        private boolean sex;
        private Role role;
        private String parent_email;

}
