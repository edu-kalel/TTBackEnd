package escom.ttbackend.controller;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.presentation.dto.*;
import escom.ttbackend.service.implementation.NutriService;
import escom.ttbackend.service.implementation.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Returns a list of Patients under the Nutritionist Watch")
    public ResponseEntity<List<UserDTO>> getAllPatients() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<UserDTO> patients = userService.getUsersByParentEmail(user.getEmail());
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @PostMapping("/appointment")
    @Operation(summary = "Schedule an Appointment")
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        AppointmentDTO appointment = userService.scheduleAppointment(appointmentRequest, user.getEmail(), AppointmentStatus.CONFIRMED);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PostMapping("/new-patient")
    @Operation(summary = "Add New Patient")
    public ResponseEntity<UserDTO> registerNewPatient(@RequestBody PatientRegistrationByNutritionistDTO registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(nutriService.registerNewPatient(registerRequest,user), HttpStatus.CREATED); //TODO check, is this ok? passing the user i mean, theres a reason why i only pass the email
    }

    @PostMapping("/diet_plan")
    @Operation(summary = "Creates or updates Diet Plan for patient")
    public ResponseEntity<String> newDietPlan(@RequestBody DietPlanDTO request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(nutriService.addNewDietPlan(request,user), HttpStatus.CREATED);
    }

    @PostMapping("/patient-record")
    @Operation(summary = "Adds a patient record (height, weight, comment)")
    public ResponseEntity<String> newPatientRecord(@RequestBody PatientRecordRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        return new ResponseEntity<>(nutriService.addPatientRecord(request, nutritionist.getEmail()), HttpStatus.CREATED);
    }

    @GetMapping("/appointments")
    @Operation(summary = "Returns a list of all appointments for nutritionist (Confirmed or not)")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<AppointmentDTO> appointments = nutriService.getAllApointments(user.getEmail());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PutMapping("/update-patient")
    @Operation(summary = "Updates Patient Info")
    public ResponseEntity<Object> updatePatient(@RequestBody PatientRegistrationByNutritionistDTO registrationDTO){
        if (!userService.existsById(registrationDTO.getEmail()))
            throw new UsernameNotFoundException("Patient does not exist");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(registrationDTO.getEmail(), user.getEmail()))
            return new ResponseEntity<>(nutriService.updatePatient(registrationDTO), HttpStatus.CREATED);
        else throw new BadCredentialsException("No permissions over this user");
    }

    @PutMapping("/confirm-appointment/{appointmentId}")
    @Operation(summary = "Confirms a solicited appointment by patient")
    public ResponseEntity<String> confirmAppointment(@PathVariable Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        nutriService.confirmAppointment(appointmentId, nutritionist);
        return new ResponseEntity<>("Appointment confirmed", HttpStatus.OK);
    }

    @DeleteMapping("/delete-appointment/{appointmentId}")
    @Operation(summary = "Cancels and deletes an appointment")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        nutriService.deleteAppointment(appointmentId, nutritionist);
        return new ResponseEntity<>("Appointment deleted", HttpStatus.OK);
    }

//    @PutMapping("/update-appointment") //TODO check this man, still not so sure about it
//    public ResponseEntity<AppointmentDTO> updateAppointment(@RequestBody AppointmentRequest appointmentRequest){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User nutritionist = (User) authentication.getPrincipal();
//        AppointmentDTO appointment = userService.scheduleAppointment(appointmentRequest, nutritionist.getEmail(), AppointmentStatus.CONFIRMED);
//        return new ResponseEntity<>(appointment, HttpStatus.OK);
//    }

//    @DeleteMapping("/appointment/{startingTime}")
//    public ResponseEntity<String> deleteAppointment(@PathVariable LocalDateTime startingTime) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User nutritionist = (User) authentication.getPrincipal();
//        nutriService.deleteAppointment(nutritionist, startingTime);
//        return new ResponseEntity<>("Appointment deleted", HttpStatus.OK);
//    }

    @DeleteMapping("/patient/{email}")
    @Operation(summary = "Deletes a patient")
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
    @Operation(summary = "This nutritionist info")
    public ResponseEntity<UserDTO> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(userService.getUserDTObyID(user.getEmail()), HttpStatus.OK);
    }

    @GetMapping("/patient/{email}")
    @Operation(summary = "Single patient info")
    public ResponseEntity<UserDTO> getPatient(@PathVariable String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutri = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(email, nutri.getEmail())){
            return new ResponseEntity<>(userService.getUserDTObyID(email), HttpStatus.OK);
        }
        else throw new BadCredentialsException("No permissions over this user");
    }

    @GetMapping("/{patientEmail}/posts")
    @Operation(summary = "Returns list of posts made by a patient")
    public ResponseEntity<List<PostDTO>> getPosts(@PathVariable String patientEmail){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutri = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(patientEmail, nutri.getEmail())){
            List<PostDTO> posts = userService.getPostsByPatient(patientEmail);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        else throw new BadCredentialsException("No permissions over this user");
    }

    @GetMapping("/{patientEmail}/patientRecords")
    @Operation(summary = "Returns list of Patient Records")
    public ResponseEntity<List<PatientRecordResponse>> getPatientRecords(@PathVariable String patientEmail){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutri = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(patientEmail, nutri.getEmail())){
            List<PatientRecordResponse> patientRecords = userService.getPatientRecords(patientEmail);
            return new ResponseEntity<>(patientRecords, HttpStatus.OK);
        }
        else throw new BadCredentialsException("No permissions over this user");
    }

    @GetMapping("/appointments/solicited")
    @Operation(summary = "List of solicited appointments by patients")
    public ResponseEntity<List<AppointmentDTO>> getSolicitedAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<AppointmentDTO> appointments = nutriService.getAppointmentsByStatus(user.getEmail(), AppointmentStatus.SOLICITED);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/confirmed")
    @Operation(summary = "List of confirmed appointments")
    public ResponseEntity<List<AppointmentDTO>> getConfirmedAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<AppointmentDTO> appointments = nutriService.getAppointmentsByStatus(user.getEmail(), AppointmentStatus.CONFIRMED);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/today-confirmed-appointments")
    @Operation(summary = "List of confirmed appointments for today")
    public ResponseEntity<List<AppointmentDTO>> getTodayConfirmedAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<AppointmentDTO> appointments = nutriService.getTodayConfirmedAppointments(user.getEmail());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

}
