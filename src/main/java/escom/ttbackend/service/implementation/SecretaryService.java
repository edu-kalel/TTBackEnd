package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.model.enums.Role;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.AppointmentDTO;
import escom.ttbackend.presentation.dto.AppointmentRequest;
import escom.ttbackend.presentation.dto.AppointmentRequestFull;
import escom.ttbackend.presentation.dto.PatientRegistrationBySecretaryDTO;
import escom.ttbackend.presentation.dto.SimpleAppointmentDTO;
import escom.ttbackend.presentation.dto.StaffRegistrationDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.repository.AppointmentRepository;
import escom.ttbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final NutriService nutriService;
    private final EmailService emailService;

    public void registerNewPatient(PatientRegistrationBySecretaryDTO request, String secretaryMail) {
        if (userRepository.existsById(request.getEmail())){
            throw new EntityExistsException("El paciente ya existe");
        }
        var nutritionist = userRepository.findById(request.getParent_email())
          .orElseThrow(() -> new UsernameNotFoundException("Nutriólogo no existe"));
        if (!userService.validateParentEmailForUser(nutritionist.getEmail(), secretaryMail)) {
            throw new BadCredentialsException("No permissions over nutritionist");
        }
        else{
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
            userRepository.save(user);
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

    public void updatePatient(PatientRegistrationBySecretaryDTO request, String secretaryEmail) {
        var user = userRepository.findById(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        var nutritionist = userRepository.findById(request.getParent_email())
                .orElseThrow(() -> new UsernameNotFoundException("Nutritionist does not exist"));
        if (!userService.validateGrandpaEmailForUser(request.getEmail(), secretaryEmail)){
            throw new BadCredentialsException("No permissions over this user");
        }
        if(request.getPassword().isBlank()) {
            user = User.builder().password(
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

        userRepository.save(user);
    }

    public List<UserDTO> getUsersByParentEmail(String nutritionistEmail, User secretary) {
        if (!userService.existsById(nutritionistEmail)){
            throw new UsernameNotFoundException("Nutri does not exist");
        }
        if (!userService.isFromClinic(secretary.getClinic(), nutritionistEmail))
            throw new BadCredentialsException("No permissions over this nutri (not same clinic)");
        if (!userService.validateParentEmailForUser(nutritionistEmail, secretary.getEmail())) {
            throw new BadCredentialsException("No permissions over this nutri");
        }
        return userService.getUsersByParentEmail(nutritionistEmail);
    }

    public void deletePatient(String patientEmail, String secretaryEmail) {
        if (!userService.existsById(patientEmail))
            throw new UsernameNotFoundException("Patient does not exist");
        if (userService.validateGrandpaEmailForUser(patientEmail, secretaryEmail)){
            throw new BadCredentialsException("No permissions over this user");
        }
        userService.deletePatient(patientEmail);
    }

    public List<AppointmentDTO> getAllApointmentsByNutritionist(String secretaryEmail, String nutritionistEmail) {
        if (!userService.existsById(nutritionistEmail)){
            throw new UsernameNotFoundException("Nutriólogo does not exist");
        }
        if (!userService.validateParentEmailForUser(nutritionistEmail, secretaryEmail))
            throw new BadCredentialsException("No permissions over nutri " + nutritionistEmail);
        return nutriService.getAllApointments(nutritionistEmail);
    }

    public List<SimpleAppointmentDTO> getAppointmentsByStatusAndNutritionist(String secretaryEmail, AppointmentStatus appointmentStatus, String nutritionistEmail) {
        if (!userService.existsById(nutritionistEmail)){
            throw new UsernameNotFoundException("Nutriólogo does not exist");
        }
        if (!userService.validateParentEmailForUser(nutritionistEmail, secretaryEmail))
            throw new BadCredentialsException("No permissions over nutri " + nutritionistEmail);
        return nutriService.getAppointmentsByStatus(nutritionistEmail, appointmentStatus);
    }

    public AppointmentDTO scheduleAppointment(AppointmentRequestFull appointmentRequest, String secretaryEmail, AppointmentStatus appointmentStatus) {
        if (!userService.existsById(appointmentRequest.getNutritionist_email())){
            throw new UsernameNotFoundException("Nutriólogo does not exist");
        }
        if (!userService.validateParentEmailForUser(appointmentRequest.getNutritionist_email(), secretaryEmail))
            throw new BadCredentialsException("No permissions over nutri " + appointmentRequest.getNutritionist_email());
        AppointmentRequest newAppointmentRequest = new AppointmentRequest(
          appointmentRequest.getPatient_email(),
          appointmentRequest.getStarting_time(),
          appointmentRequest.getEnding_time()
        );
        return userService.scheduleAppointment(newAppointmentRequest, appointmentRequest.getNutritionist_email(), appointmentStatus);
    }

    public void confirmAppointment(Long appointmentId, String secretaryEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("Appointment does not exist"));
        var nutritionist = appointment.getNutritionist();
        if (!userService.existsById(nutritionist.getEmail())){
            throw new UsernameNotFoundException("Nutriólogo does not exist");
        }
        if (!userService.validateParentEmailForUser(nutritionist.getEmail(), secretaryEmail))
            throw new BadCredentialsException("No permissions over nutri " + nutritionist.getEmail());
        nutriService.confirmAppointment(appointmentId, nutritionist);
    }

    public void deleteAppointment(Long appointmentId, String secretaryEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("Appointment does not exist"));
        var nutritionist = appointment.getNutritionist();
        if (!userService.existsById(nutritionist.getEmail())){
            throw new UsernameNotFoundException("Nutriólogo does not exist");
        }
        if (!userService.validateParentEmailForUser(nutritionist.getEmail(), secretaryEmail))
            throw new BadCredentialsException("No permissions over nutri " + nutritionist.getEmail());
        if (appointment.getNutritionist().getEmail().equals(nutritionist.getEmail())){
            User patient = appointment.getPatient();
            emailService.sendsAppointementDeletionMessage(nutritionist, patient, appointment);
            appointmentRepository.deleteById(appointmentId);
        }
    }
}
