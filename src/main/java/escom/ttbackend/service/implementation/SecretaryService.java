package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.Role;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.StaffRegistrationDTO;
import escom.ttbackend.presentation.dto.PatientRegistrationBySecretaryDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.repository.AppointmentRepository;
import escom.ttbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecretaryService {
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final Mapper mapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserDTO registerNewPatient(PatientRegistrationBySecretaryDTO request) {
        if (userRepository.existsById(request.getEmail())){
            throw new EntityExistsException("User Already Exists");
        }
        else{
            var nutritionist = userRepository.findById(request.getParent_email())
                    .orElseThrow(() -> new UsernameNotFoundException("Nutritionist does not exist"));
            var user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.PATIENT)
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.getSex())
                    .parent(nutritionist)
                    .ailments(request.getAilments())
                    .clinic(nutritionist.getClinic())
                    .build();
            return mapper.mapToUserDTO(userRepository.save(user));
        }
    }

    public UserDTO registerNewNutritionist(StaffRegistrationDTO request, User secretary) {
        if (userRepository.existsById(request.getEmail())){
            throw new EntityExistsException("User Already Exists");
        }
        else{
            var user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.NUTRITIONIST)
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.getSex())
                    .parent(secretary)
                    .clinic(secretary.getClinic())
                    .build();
            return mapper.mapToUserDTO(userRepository.save(user));
        }
    }

    public Object updatePatient(PatientRegistrationBySecretaryDTO request) {
        var user = userRepository.findById(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var nutritionist = userRepository.findById(request.getParent_email())
                .orElseThrow(() -> new UsernameNotFoundException("Nutritionist does not exist"));
        if(request.getPassword().isBlank()) {
            user = User.builder().password(
//                            userRepository.findByEmail(request.getEmail()).get().getPassword()
                            user.getPassword()
                    )
                    .clinic(user.getClinic())
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .role(Role.PATIENT)
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.getSex())
                    .ailments(request.getAilments())
                    .parent(nutritionist)
                    .build();
        } else {
            user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.PATIENT)
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.getSex())
                    .ailments(request.getAilments())
                    .parent(nutritionist)
                    .clinic(user.getClinic())
                    .build();
        }

        return mapper.mapToUserDTO(userRepository.save(user));
    }
}
