package escom.ttbackend.config;

import escom.ttbackend.presentation.dto.RegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    /**
     * Injected AuthenticationService class.
     */
    private final AuthenticationService service;

    /**
     * Registers a new entry of Staff to the system.
     *
     * @param registerRequest The RegisterRequest containing the new Staff data.
     * @return {@code ResponseEntity} containing the new Staff details.
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegistrationDTO registerRequest){
        System.out.println(registerRequest); //TODO remove this debug part
        return new ResponseEntity<>(service.register(registerRequest), HttpStatus.CREATED);
    }

    /**
     * Update of a single Staff given the new Staff details.
     *
     * @param newRegister The new Staff with the updated data.
     * @return {@code ResponseEntity} containing the new Staff details.
     * @throws UsernameNotFoundException If the Staff does not exist.
     */
    @PutMapping("/user/update")
    public ResponseEntity<Object> update(@RequestBody RegisterRequest newRegister){
        System.out.println(newRegister);
        return new ResponseEntity<>(service.update(newRegister), HttpStatus.CREATED);
    }

    /**
     * Authenticates the credentials of a Staff member that tries to
     * log in to the System.
     *
     * @param authenticationRequest The AuthenticationRequest containing an
     *                              email and a password.
     * @return {@code ResponseEntity} containing the AuthenticationResponse details,
     * including the JWT token.
     * @throws UsernameNotFoundException If the Staff does not exist.
     * @throws BadCredentialsException If the email and password do not match.
     * @see AuthenticationResponse
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        System.out.println(authenticationRequest);
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }
}
