package escom.ttbackend.config;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.Role;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.*;
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
    public void registerNewClinic(ClinicRegistrationDTO request) {
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
                .sex(request.getSex())
                .clinic(request.getClinic())
                .build();
        mapper.mapToUserDTO(userRepository.save(user));
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findById(request.getEmail())
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
                .email(user.getEmail())
                .build();
    }
}
