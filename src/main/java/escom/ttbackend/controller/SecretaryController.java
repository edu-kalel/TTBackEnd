package escom.ttbackend.controller;

import escom.ttbackend.config.AuthenticationService;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.dto.*;
import escom.ttbackend.service.implementation.SecretaryService;
import escom.ttbackend.service.implementation.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/secretary")
@SecurityRequirement(name = "bearerAuth")
public class SecretaryController {
    private final SecretaryService secretaryService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public SecretaryController(SecretaryService secretaryService, AuthenticationService authenticationService, UserService userService) {
        this.secretaryService = secretaryService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/new-patient")
    public ResponseEntity<UserDTO> registerNewPatient(@RequestBody PatientRegistrationBySecretaryDTO registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(registerRequest.getParent_email(), secretary.getEmail()))
            return new ResponseEntity<>(secretaryService.registerNewPatient(registerRequest), HttpStatus.CREATED);
        else throw new ResponseStatusException(HttpStatus.FORBIDDEN); //TODO improve this
    }

    @PostMapping("/new-nutritionist")
    public ResponseEntity<UserDTO> registerNewNutritionist(@RequestBody NutritionistRegistrationDTO registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        return new ResponseEntity<>(secretaryService.registerNewNutritionist(registerRequest,secretary), HttpStatus.CREATED);
    }

    @GetMapping("/patients-by-nutritionist/{nutritionistEmail}")
    public ResponseEntity<List<UserDTO>> getAllPatients(@PathVariable String nutritionistEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        if (!userService.isFromSameClinic(secretary.getClinic(), nutritionistEmail))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //TODO lol also improve this (this is getting out of hand)
        if (userService.validateParentEmailForUser(nutritionistEmail, secretary.getEmail())) {
            List<UserDTO> patients = userService.getUsersByParentEmail(nutritionistEmail);
            return new ResponseEntity<>(patients, HttpStatus.OK);
        }
        else throw new ResponseStatusException(HttpStatus.FORBIDDEN);//TODO improoooove
    }

    @DeleteMapping("/patient/{email}")
    public ResponseEntity<String> deletePatient(@PathVariable String email){
        if (!userService.existsById(email))
            throw new UsernameNotFoundException("Patient does not exist");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        if (userService.validateGrandpaEmailForUser(email, secretary.getEmail())){
            userService.delete(email);
            return new ResponseEntity<>("Patient deleted", HttpStatus.OK);
        }
        else throw new ResponseStatusException(HttpStatus.FORBIDDEN); //TODO improve this
    }

    @GetMapping("/nutritionists")
    public ResponseEntity<List<SimpleUserDTO>> getNutritionists(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        List<SimpleUserDTO> nutritionists = userService.getSimpleUserDTOsByParentEmail(secretary.getEmail());
        return new ResponseEntity<>(nutritionists, HttpStatus.OK);
    }

    @PutMapping("/update-patient")
    public ResponseEntity<Object> update(@RequestBody PatientRegistrationBySecretaryDTO registrationDTO){
        if (!userService.existsById(registrationDTO.getEmail()))
            throw new UsernameNotFoundException("Patient does not exist");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        if (userService.validateGrandpaEmailForUser(registrationDTO.getEmail(), secretary.getEmail()))
            return new ResponseEntity<>(secretaryService.updatePatient(registrationDTO), HttpStatus.CREATED);
        else throw new ResponseStatusException(HttpStatus.FORBIDDEN); //TODO improve this
    }
}
