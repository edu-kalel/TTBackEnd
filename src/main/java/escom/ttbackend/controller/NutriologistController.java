package escom.ttbackend.controller;

import escom.ttbackend.config.AuthenticationService;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.dto.AppointmentDTO;
import escom.ttbackend.presentation.dto.AppointmentRequest;
import escom.ttbackend.presentation.dto.RegistrationDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.service.implementation.NutriService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nutri/")
@SecurityRequirement(name = "bearerAuth")
public class NutriologistController {
    private final NutriService nutriService;
    private final AuthenticationService authenticationService;
//    private final

    public NutriologistController(NutriService userServiceNutri, AuthenticationService authenticationService) {
        this.nutriService = userServiceNutri;
        this.authenticationService = authenticationService;
    }

    @GetMapping("patients")
    public ResponseEntity<List<UserDTO>> getAllPatients() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<UserDTO> patients = nutriService.getUsersByParentEmail(user.getEmail());
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @PostMapping("appointment")
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        AppointmentDTO appointment = nutriService.scheduleAppointment(appointmentRequest, user.getEmail());
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping("new-patient")
    public ResponseEntity<Object> registerNewPatient(@RequestBody RegistrationDTO registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println(registerRequest); //TODO remove this debug part
        return new ResponseEntity<>(authenticationService.registerNewPatient(registerRequest,user), HttpStatus.CREATED);
    }

    @GetMapping("appointments")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<AppointmentDTO> appointments = nutriService.getAllApointments(user.getEmail());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }


}
