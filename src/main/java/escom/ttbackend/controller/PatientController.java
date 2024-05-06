package escom.ttbackend.controller;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.presentation.dto.AppointmentDTO;
import escom.ttbackend.presentation.dto.AppointmentRequest;
import escom.ttbackend.presentation.dto.PostDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.service.implementation.PatientService;
import escom.ttbackend.service.implementation.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@SecurityRequirement(name = "bearerAuth")
public class PatientController {
    private final UserService userService;
    private final PatientService patientService;

    public PatientController(UserService userService, PatientService patientService) {
        this.userService = userService;
        this.patientService = patientService;
    }

    @PostMapping("/post")
    public ResponseEntity<PostDTO> newPost(@RequestBody String content){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(patientService.newPost(content, user.getEmail()), HttpStatus.OK);
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserDTO> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(userService.getUserDTObyID(user.getEmail()), HttpStatus.OK); //TODO return a better suited DTO
    }

    @PostMapping("/appointment")
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        appointmentRequest.setPatient_email(user.getEmail());
        AppointmentDTO appointment = userService.scheduleAppointment(appointmentRequest, user.getParent().getEmail(), AppointmentStatus.SOLICITED);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }
}
