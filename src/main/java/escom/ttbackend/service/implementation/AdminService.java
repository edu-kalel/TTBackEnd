package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Aliment;
import escom.ttbackend.model.entities.AlimentGroup;
import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.Role;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.AlimentCreationDTO;
import escom.ttbackend.presentation.dto.AlimentGroupDTO;
import escom.ttbackend.presentation.dto.ReassignationRequest;
import escom.ttbackend.presentation.dto.StaffRegistrationDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.repository.AlimentGroupRepository;
import escom.ttbackend.repository.AlimentRepository;
import escom.ttbackend.repository.AppointmentRepository;
import escom.ttbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.Fraction;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;
    private final AlimentGroupRepository alimentGroupRepository;
    private final AlimentRepository alimentRepository;

    public UserDTO registerNewUser(StaffRegistrationDTO request, String clinic) {
        if (userRepository.existsById(request.getEmail())){
            throw new EntityExistsException("User Already Exists");
        }
        else if (!request.getParent_email().isBlank()){
            var parent = userRepository.findById(request.getParent_email())
                    .orElseThrow(() -> new UsernameNotFoundException("Parent User does not exist"));
            if (!userService.isFromClinic(clinic, parent.getEmail()))
                throw new BadCredentialsException("Parent User is not from same Clinic");
            var user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.getSex())
                    .parent(parent)
                    .clinic(clinic)
                    .build();
            return mapper.mapToUserDTO(userRepository.save(user));
        }
        else {
            var user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.getSex())
                    .clinic(clinic)
                    .build();
            return mapper.mapToUserDTO(userRepository.save(user));
        }
    }

    public UserDTO updateUser(StaffRegistrationDTO request) {
        var user = userRepository.findById(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        user.setFirst_name(request.getFirst_name());
        user.setLast_name(request.getLast_name());
        user.setPhone(request.getPhone());
        if (!request.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (!request.getParent_email().isBlank()){
            var parent = userRepository.findById(request.getParent_email())
                    .orElseThrow(() -> new UsernameNotFoundException("Parent User does not exist"));
            user.setParent(parent);
        }
        user.setRole(request.getRole());
        user.setDate_of_birth(request.getDate_of_birth());

        return mapper.mapToUserDTO(userRepository.save(user));
    }

    public List<UserDTO> getStaffByClinic(String clinic) {
        List<UserDTO> allUsers = userRepository.findByClinic(clinic)
                .stream()
                .map(mapper::mapToUserDTO)
                .collect(Collectors.toList());

        return allUsers.stream()
                .filter(user -> isStaffRole(user.getRole()))
                .collect(Collectors.toList());
    }

    private boolean isStaffRole(Role role) {
        return role == Role.NUTRITIONIST || role == Role.NUTRITIONIST_ADMIN ||
                role == Role.SECRETARY || role == Role.SECRETARY_ADMIN;
    }

    public void deleteUser(String email) {
        var user = userRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        if (user.getRole().equals(Role.NUTRITIONIST)||user.getRole().equals(Role.NUTRITIONIST_ADMIN)){
            List<Appointment> appointments = appointmentRepository.findByNutritionist_Email(email);
            appointmentRepository.deleteAll(appointments);
            List<User> patients = userRepository.findByParent_Email(email);
            for (User patient : patients){
                patient.setParent(null);
                userRepository.save(patient);
            }
        }
        userRepository.deleteById(email);
    }

    public UserDTO reassignParent(ReassignationRequest request) {
        var child = userRepository.findById(request.getChild_email())
                .orElseThrow(() -> new UsernameNotFoundException("Child User does not exist"));
        var parent = userRepository.findById(request.getParent_email())
                .orElseThrow(() -> new UsernameNotFoundException("Parent User does not exist"));

        child.setParent(parent);

        return mapper.mapToUserDTO(userRepository.save(child));
    }

    public List<UserDTO> getUsersByClinic(String clinic) {
        return userRepository.findByClinic(clinic)
                .stream()
                .map(mapper::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public void addNewAlimentGroup(AlimentGroupDTO request) {
        var group = AlimentGroup.builder()
          .name(request.getName())
          .kcal(request.getKcal())
          .carbs(request.getCarbs())
          .fats(request.getFats())
          .proteins(request.getProteins())
          .build();
        alimentGroupRepository.save(group);
    }

    public void addNewAliment(AlimentCreationDTO request) {
        var group = alimentGroupRepository.findByName(request.getGroupName());
        Fraction fraction;
        if (request.getDenominator()==0){
            fraction = Fraction.getFraction(request.getWhole());
        }
        else {
            fraction = Fraction.getFraction(request.getWhole(), request.getNumerator(), request.getDenominator());
        }
        var aliment = Aliment.builder()
          .name(request.getName())
          .quantity(fraction)
          .unit(request.getUnit())
          .alimentGroup(group)
          .build();
        alimentRepository.save(aliment);
    }
}
