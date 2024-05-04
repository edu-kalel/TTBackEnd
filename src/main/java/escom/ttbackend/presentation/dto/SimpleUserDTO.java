package escom.ttbackend.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleUserDTO {
    private String email;
    private String first_name;
    private String last_name;
}
