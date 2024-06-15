package escom.ttbackend.controller;

import escom.ttbackend.config.AuthenticationService;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.presentation.dto.*;
import escom.ttbackend.service.implementation.SecretaryService;
import escom.ttbackend.service.implementation.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secretary")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SecretaryController {
    private final SecretaryService secretaryService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

//    public SecretaryController(SecretaryService secretaryService, AuthenticationService authenticationService, UserService userService) {
//        this.secretaryService = secretaryService;
//        this.authenticationService = authenticationService;
//        this.userService = userService;
//    }

    @GetMapping("/patient/info/{email}")
    @Operation(summary = "Single patient info")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secre = (User) authentication.getPrincipal();
        if (userService.validateGrandpaEmailForUser(email, secre.getEmail())){
            return new ResponseEntity<>(userService.getPatientDTObyID(email), HttpStatus.OK);
        }
        else throw new BadCredentialsException("No tienes permisos sobre este usuario");
    }

    @GetMapping("/nutri/info/{email}")
    @Operation(summary = "Single nutritionist info")
    public ResponseEntity<UserDTO> getNutri(@PathVariable String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secre = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(email, secre.getEmail())){
            return new ResponseEntity<>(userService.getUserDTObyID(email), HttpStatus.OK);
        }
        else throw new BadCredentialsException("No tienes permisos sobre este usuario");
    }

    @GetMapping("/appointments/{nutritionistEmail}")
    @Operation(summary = "Returns a list of all appointments for nutritionist (Confirmed or not)")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByNutritionist(@PathVariable String nutritionistEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<AppointmentDTO> appointments = secretaryService.getAllApointmentsByNutritionist(user.getEmail(), nutritionistEmail);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/solicited/{nutritionistEmail}")
    @Operation(summary = "List of solicited appointments by patients for nutritionist")
    public ResponseEntity<List<SimpleAppointmentDTO>> getSolicitedAppointmentsForNutri(@PathVariable String nutritionistEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<SimpleAppointmentDTO> appointments = secretaryService.getAppointmentsByStatusAndNutritionist(user.getEmail(), AppointmentStatus.SOLICITED, nutritionistEmail);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/confirmed/{nutritionistEmail}")
    @Operation(summary = "List of confirmed appointments for nutritionist")
    public ResponseEntity<List<SimpleAppointmentDTO>> getConfirmedAppointmentsByNutri(@PathVariable String nutritionistEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<SimpleAppointmentDTO> appointments = secretaryService.getAppointmentsByStatusAndNutritionist(user.getEmail(), AppointmentStatus.CONFIRMED, nutritionistEmail);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/patients-by-nutritionist/{nutritionistEmail}")
    public ResponseEntity<List<UserDTO>> getAllPatients(@PathVariable String nutritionistEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        List<UserDTO> patients = secretaryService.getUsersByParentEmail(nutritionistEmail, secretary);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/nutritionists")
    public ResponseEntity<List<SimpleUserDTO>> getNutritionists(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        List<SimpleUserDTO> nutritionists = userService.getSimpleUserDTOsByParentEmail(secretary.getEmail());
        return new ResponseEntity<>(nutritionists, HttpStatus.OK);
    }

    @PostMapping("/new-patient")
    public ResponseEntity<String> registerNewPatient(@RequestBody PatientRegistrationBySecretaryDTO registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        secretaryService.registerNewPatient(registerRequest, secretary.getEmail());
        return new ResponseEntity<>("Paciente Creado Correctamente", HttpStatus.CREATED);
    }

    @PostMapping("/appointment")
    @Operation(summary = "Schedule an Appointment")
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody AppointmentRequestFull appointmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        AppointmentDTO appointment = secretaryService.scheduleAppointment(appointmentRequest, user.getEmail(), AppointmentStatus.CONFIRMED);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PutMapping("/appointment/confirm/{appointmentId}")
    @Operation(summary = "Confirms a solicited appointment by patient")
    public ResponseEntity<String> confirmAppointment(@PathVariable Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        secretaryService.confirmAppointment(appointmentId, secretary.getEmail());
        return new ResponseEntity<>("Cita confirmada", HttpStatus.OK);
    }

//    @PostMapping("/new-nutritionist")
//    public ResponseEntity<UserDTO> registerNewNutritionist(@DietRequestBody NutritionistRegistrationDTO registerRequest){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User secretary = (User) authentication.getPrincipal();
//        return new ResponseEntity<>(secretaryService.registerNewNutritionist(registerRequest,secretary), HttpStatus.CREATED);
//    }

    @DeleteMapping("/appointment/delete/{appointmentId}")
    @Operation(summary = "Cancels and deletes an appointment")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        secretaryService.deleteAppointment(appointmentId, secretary.getEmail());
        return new ResponseEntity<>("Cita eliminada", HttpStatus.OK);
    }


    @DeleteMapping("/patient/{email}")
    public ResponseEntity<String> deletePatient(@PathVariable String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        secretaryService.deletePatient(email, secretary.getEmail());
        return new ResponseEntity<>("Patient deleted", HttpStatus.OK);
        }



    @PutMapping("/patient/update")
    public ResponseEntity<String> update(@RequestBody PatientRegistrationBySecretaryDTO registrationDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User secretary = (User) authentication.getPrincipal();
        secretaryService.updatePatient(registrationDTO, secretary.getEmail());
        return new ResponseEntity<>("Usuario Actualizado con éxito", HttpStatus.CREATED);
        }
}
