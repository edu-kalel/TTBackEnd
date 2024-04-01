package escom.ttbackend.presentation;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.dto.RegistrationDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    private final UserService userService;

    public Mapper(UserService userService) {
        this.userService = userService;
    }

    public UserDTO mapToUserDTO(User user){
        return new UserDTO(
                user.getEmail(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getDate_of_birth(),
                user.getPhone(),
                user.isSex(),
                user.getRole()
        );
    }
//    public User mapToNutritionist(RegistrationDTO registrationDTO){
//        User secretary = userService.getByEmail(registrationDTO.getParent_email());
//        var user = User.builder()
//                .email(registrationDTO.getEmail())
//                .first_name(registrationDTO.getFirst_name())
//                .last_name(registrationDTO.getLast_name())
//                .date_of_birth(registrationDTO.getDate_of_birth())
//                .phone(registrationDTO.getPhone())
//                .
//    }
}
