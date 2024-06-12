package escom.ttbackend.controller;

import escom.ttbackend.config.AuthenticationRequest;
import escom.ttbackend.config.AuthenticationResponse;
import escom.ttbackend.config.AuthenticationService;
import escom.ttbackend.presentation.dto.ClinicRegistrationDTO;
import escom.ttbackend.service.implementation.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    /**
     * Injected AuthenticationService class.
     */
    private final AuthenticationService service;
    private final UserService userService;

    /**
     * Registers a new entry of Staff to the system.
     *
     * @param registerRequest The RegisterRequest containing the new Staff data.
     * @return {@code ResponseEntity} containing the new Staff details.
     */
    @PostMapping("/new-clinic/register")
    public ResponseEntity<Object> registerNewClinic(@RequestBody ClinicRegistrationDTO registerRequest){
        service.registerNewClinic(registerRequest);
        return new ResponseEntity<>("Registro exitoso, regresa e inicia sesión con tus credenciales", HttpStatus.CREATED);
    }

    /*@PostMapping("/user/register")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Object> register(@DietRequestBody RegistrationDTO registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (userService.isFromClinic(user.getClinic(), registerRequest.getParent_email()))
            return new ResponseEntity<>(service.register(registerRequest,user.getClinic()), HttpStatus.CREATED);
        else
            return new ResponseEntity<>("Parent Email is not from same clinic", HttpStatus.UNAUTHORIZED);
    }*/


    /**
     * Update of a single Staff given the new Staff details.
     *
     * @param newRegister The new Staff with the updated data.
     * @return {@code ResponseEntity} containing the new Staff details.
     * @throws UsernameNotFoundException If the Staff does not exist.
     */
    /*@PutMapping("/user/update")
    @Operation(summary = "My endpoint for updating", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Object> update(@DietRequestBody RegistrationDTO newRegister){
        System.out.println(newRegister);
        return new ResponseEntity<>(service.update(newRegister), HttpStatus.CREATED);
    }*/

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
