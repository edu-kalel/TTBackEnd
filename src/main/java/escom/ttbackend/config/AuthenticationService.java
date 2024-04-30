package escom.ttbackend.config;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.Role;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.RegistrationDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Mapper mapper;
    public UserDTO registerNewClinic(RegistrationDTO request) {
        if (userRepository.existsById(request.getEmail())){
            throw new EntityExistsException("User Already Exists");
        }
        if (userRepository.existsByClinic(request.getClinic())){
            throw new EntityExistsException("Clinic Already Exists");
        }
        var user = User.builder()
                .email(request.getEmail())
                .first_name(request.getFirst_name())
                .last_name(request.getLast_name())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .date_of_birth(request.getDate_of_birth())
                .sex(request.isSex())
                .clinic(request.getClinic())
                .build();
        return mapper.mapToUserDTO(userRepository.save(user));
    }
    public UserDTO register(RegistrationDTO request, String clinic) {
        if (userRepository.existsById(request.getEmail())){
            throw new EntityExistsException("User Already Exists");
        }
        else if ((request.getRole().equals(Role.SECRETARY_ADMIN))||(request.getRole().equals(Role.SECRETARY))){
            var user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.isSex())
                    .clinic(clinic)
                    .build();
            return mapper.mapToUserDTO(userRepository.save(user));
        }
        else if ((request.getRole().equals(Role.NUTRITIONIST))||(request.getRole().equals(Role.NUTRITIONIST_ADMIN))){
            var secretary = userRepository.findByEmail(request.getParent_email())
                    .orElseThrow(() -> new UsernameNotFoundException("Secretary does not exist"));
            var user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.isSex())
                    .parent(secretary)
                    .clinic(clinic)
                    .build();
            return mapper.mapToUserDTO(userRepository.save(user));
        }
        else if (request.getRole().equals(Role.PATIENT)){
            var nutritionist = userRepository.findByEmail(request.getParent_email())
                    .orElseThrow(() -> new UsernameNotFoundException("Nutritionist does not exist"));
            var user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.isSex())
                    .parent(nutritionist)
                    .ailments(request.getAilments())
                    .clinic(clinic)
                    .build();
            return mapper.mapToUserDTO(userRepository.save(user));
        }
        else
            throw new EntityExistsException("Something wrong man, about that ROlE");

    }

    public UserDTO update(RegisterRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        if(request.getPassword().isBlank()) {
            user = User.builder().password(
                            userRepository.findByEmail(request.getEmail()).get().getPassword()
                    )
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.isSex())
                    .build();
        } else {
            user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.isSex())
                    .build();
        }

        return mapper.mapToUserDTO(userRepository.save(user));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Incorrect Password");
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .name(user.getFirst_name())
                .build();
    }

    public UserDTO registerNewPatient(RegistrationDTO request, User parentUser) {
        if (userRepository.existsById(request.getEmail())){
            throw new EntityExistsException("User Already Exists");
        }
        else{
//            var nutritionist = userRepository.findByEmail(request.getParent_email())
//                    .orElseThrow(() -> new UsernameNotFoundException("Nutritionist does not exist"));
            var user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.isSex())
                    .parent(parentUser)
                    .ailments(request.getAilments())
                    .clinic(parentUser.getClinic())
                    .build();
            return mapper.mapToUserDTO(userRepository.save(user));
        }
    }
}
