package escom.ttbackend.presentation;

import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.dto.AppointmentDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public UserDTO mapToUserDTO(User user){
        return new UserDTO(
                user.getEmail(),
                user.getClinic(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getDate_of_birth(),
                user.getPhone(),
                user.isSex(),
                user.getRole()
        );
    }

    public AppointmentDTO mapToAppointmentDTO(Appointment appointment){
        return new AppointmentDTO(
                appointment.getNutritionist().getEmail(),
                appointment.getStarting_time(),
                appointment.getPatient().getEmail(),
                appointment.getEnding_time(),
                appointment.getAppointmentStatus()
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
