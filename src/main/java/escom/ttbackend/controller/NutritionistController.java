package escom.ttbackend.controller;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.presentation.dto.*;
import escom.ttbackend.service.implementation.NutriService;
import escom.ttbackend.service.implementation.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nutri")
@SecurityRequirement(name = "bearerAuth")
public class NutritionistController {
    private final NutriService nutriService;
    private final UserService userService;

    public NutritionistController(NutriService userServiceNutri, UserService userService) {
        this.nutriService = userServiceNutri;
        this.userService = userService;
    }

    @GetMapping("/patients")
    public ResponseEntity<List<UserDTO>> getAllPatients() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<UserDTO> patients = userService.getUsersByParentEmail(user.getEmail());
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @PostMapping("/appointment")
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        AppointmentDTO appointment = userService.scheduleAppointment(appointmentRequest, user.getEmail(), AppointmentStatus.CONFIRMED);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping("/new-patient")
    public ResponseEntity<UserDTO> registerNewPatient(@RequestBody PatientRegistrationByNutritionistDTO registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(nutriService.registerNewPatient(registerRequest,user), HttpStatus.CREATED); //TODO check, is this ok? passing the user i mean, theres a reason why i only pass the email
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<AppointmentDTO> appointments = nutriService.getAllApointments(user.getEmail());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PutMapping("/update-patient")
    public ResponseEntity<Object> updatePatient(@RequestBody PatientRegistrationByNutritionistDTO registrationDTO){
        if (!userService.existsById(registrationDTO.getEmail()))
            throw new UsernameNotFoundException("Patient does not exist");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(registrationDTO.getEmail(), user.getEmail()))
            return new ResponseEntity<>(nutriService.updatePatient(registrationDTO), HttpStatus.CREATED);
        else throw new BadCredentialsException("No permissions over this user");
    }

//    @PutMapping("/update-appointment")
//    public ResponseEntity<AppointmentDTO> updateAppointment(@RequestBody AppointmentRequest appointmentRequest){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User nutritionist = (User) authentication.getPrincipal();
//        AppointmentDTO appointment = userService.scheduleAppointment(appointmentRequest, nutritionist.getEmail(), AppointmentStatus.CONFIRMED);
//        return new ResponseEntity<>(appointment, HttpStatus.OK);
//    }

    @DeleteMapping("/patient/{email}")
    public ResponseEntity<String> deletePatient(@PathVariable String email){
        if (!userService.existsById(email))
            throw new UsernameNotFoundException("Patient does not exist");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(email, user.getEmail())){
            userService.deletePatient(email);
            return new ResponseEntity<>("Patient deleted", HttpStatus.OK);
        }
        else throw new BadCredentialsException("No permissions over this user");
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserDTO> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(userService.getUserDTObyID(user.getEmail()), HttpStatus.OK);
    }

    @GetMapping("/patient/{email}")
    public ResponseEntity<UserDTO> getPatient(@PathVariable String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutri = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(email, nutri.getEmail())){
            return new ResponseEntity<>(userService.getUserDTObyID(email), HttpStatus.OK);
        }
        else throw new BadCredentialsException("No permissions over this user");
    }

    @GetMapping("/{patientEmail}/posts")
    public ResponseEntity<List<PostDTO>> getPosts(@PathVariable String patientEmail){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutri = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(patientEmail, nutri.getEmail())){
            List<PostDTO> posts = nutriService.getPostsByPatient(patientEmail);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        else throw new BadCredentialsException("No permissions over this user");
    }

    @GetMapping("/appointments/solicited")
    public ResponseEntity<List<AppointmentDTO>> getSolicitedAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<AppointmentDTO> appointments = nutriService.getAppointmentsByStatus(user.getEmail(), AppointmentStatus.SOLICITED);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

}
