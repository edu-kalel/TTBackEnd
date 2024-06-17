package escom.ttbackend.controller;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.presentation.dto.*;
import escom.ttbackend.presentation.dto.calculation.CaloriesCalculationDTO;
import escom.ttbackend.presentation.dto.calculation.DietRequestBody;
import escom.ttbackend.presentation.dto.calculation.PortionsDTO;
import escom.ttbackend.service.implementation.NutriService;
import escom.ttbackend.service.implementation.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/nutri")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class NutritionistController {
    private final NutriService nutriService;
    private final UserService userService;

    public NutritionistController(NutriService userServiceNutri, UserService userService) {
        this.nutriService = userServiceNutri;
        this.userService = userService;
    }

    @GetMapping("/patients")
    @Operation(summary = "Returns a list of Patients under the Nutritionist Watch")
    public ResponseEntity<List<SimpleUserDTO>> getAllPatients() {
        log.info("enters get all patients in nutri controller");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<SimpleUserDTO> patients = userService.getSimpleUserDTOsByParentEmail(user.getEmail());
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/appointments")
    @Operation(summary = "Returns a list of all appointments for nutritionist (Confirmed or not)")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<AppointmentDTO> appointments = nutriService.getAllApointments(user.getEmail());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/my-info")
    @Operation(summary = "This nutritionist info")
    public ResponseEntity<UserDTO> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(userService.getUserDTObyID(user.getEmail()), HttpStatus.OK);
    }

    @GetMapping("/patient/info/{email}")
    @Operation(summary = "Single patient info")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutri = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(email, nutri.getEmail())){
            return new ResponseEntity<>(userService.getPatientDTObyID(email), HttpStatus.OK);
        }
        else throw new BadCredentialsException("No tienes permisos sobre este usuario");
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
        else throw new BadCredentialsException("No tienes permisos sobre este usuario");
    }

    @GetMapping("/patient/records/{patientEmail}")
    @Operation(summary = "Returns list of Patient Records")
    public ResponseEntity<List<PatientRecordResponse>> getPatientRecords(@PathVariable String patientEmail){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutri = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(patientEmail, nutri.getEmail())){
            List<PatientRecordResponse> patientRecords = userService.getPatientRecords(patientEmail);
            return new ResponseEntity<>(patientRecords, HttpStatus.OK);
        }
        else throw new BadCredentialsException("No tienes permisos sobre este usuario");
    }

    @GetMapping("/appointments/solicited")
    @Operation(summary = "List of solicited appointments by patients")
    public ResponseEntity<List<SimpleAppointmentDTO>> getSolicitedAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<SimpleAppointmentDTO> appointments = nutriService.getAppointmentsByStatus(user.getEmail(), AppointmentStatus.SOLICITED);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/confirmed")
    @Operation(summary = "List of confirmed appointments")
    public ResponseEntity<List<SimpleAppointmentDTO>> getConfirmedAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<SimpleAppointmentDTO> appointments = nutriService.getAppointmentsByStatus(user.getEmail(), AppointmentStatus.CONFIRMED);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/today-confirmed-appointments")
    @Operation(summary = "List of confirmed appointments for today")
    public ResponseEntity<List<SimpleAppointmentDTO>> getTodayConfirmedAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<SimpleAppointmentDTO> appointments = nutriService.getTodayConfirmedAppointments(user.getEmail());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/patient/big-info/{patientEmail}")
    @Operation(summary = "big info to be displayed in patients individual view")
    public ResponseEntity<BigPatientInfoDTO> getBigInfo(@PathVariable String patientEmail){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        return new ResponseEntity<>(nutriService.getBigInfo(patientEmail, nutritionist.getEmail()), HttpStatus.OK);
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
    public ResponseEntity<String> registerNewPatient(@RequestBody PatientRegistrationByNutritionistDTO registerRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        nutriService.registerNewPatient(registerRequest,user);          //TODO check, is this ok? passing the user i mean, theres a reason why i only pass the email
        return new ResponseEntity<>("Paciente Creado Correctamente", HttpStatus.CREATED);
    }

//    @PostMapping("/diet-plan")
//    @Operation(summary = "Creates or updates Diet Plan for patient")
//    public ResponseEntity<String> newDietPlan(@RequestBody DietPlanDTO request){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        return new ResponseEntity<>(nutriService.addNewDietPlan(request,user), HttpStatus.CREATED);
//    }


    @PostMapping("/patient-record")
    @Operation(summary = "Adds a patient record (height, weight, comment)")
    public ResponseEntity<CaloriesCalculationDTO> newPatientRecord(@RequestBody PatientRecordRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        return new ResponseEntity<>(nutriService.addPatientRecord(request, nutritionist.getEmail()), HttpStatus.CREATED);
    }


    @PostMapping("/calculate-portions/{patientEmail}")
    @Operation(summary = "Calculates portions")
    public ResponseEntity<PortionsDTO> calculatePortions(@RequestBody DietRequestBody request, @PathVariable String patientEmail) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        return new ResponseEntity<>(nutriService.calculatePortions(request, nutritionist.getEmail(), patientEmail), HttpStatus.OK);
    }


    // diet plan
    @PostMapping("/diet-plan/new/{patientEmail}")
    @Operation(summary = "*creates* new diet plan and returns id")
    public ResponseEntity<Long> newDietPlan(@PathVariable String patientEmail){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        return new ResponseEntity<>(nutriService.createNewDietPlanEntity(nutritionist.getEmail(), patientEmail), HttpStatus.OK);
    }

    @PostMapping("/diet-plan/add-meals")
    @Operation(summary = "adds meals to diet plan")
    public ResponseEntity<String> addsMeals(@RequestBody MealsToAddDTO request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        nutriService.addMealsToDietPlan(request);
        return new ResponseEntity<>("Añadidas", HttpStatus.OK);
    }

    @PutMapping("/diet-plan/finish")
    @Operation(summary = "finish the creation of diet plan and persists it")
    public ResponseEntity<String> finishDietPlan(@RequestBody FinishDietPlanDTO request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        nutriService.finishDietPlan(request);
        return new ResponseEntity<>("Hecho!", HttpStatus.OK);
    }

    @GetMapping("/diet-plan/get-all-aliments")
    @Operation(summary = "gets list of all aliments")
    public ResponseEntity<List<AlimentDTO>> getAllAliments(){
        List<AlimentDTO> list = nutriService.getAllAliments();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/patient/diet-plan-list/{patientEmail}")
    public ResponseEntity<List<SimpleDietPlanDTO>> getDietPlansListByPatient(@PathVariable String patientEmail){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        List<SimpleDietPlanDTO> list = nutriService.getDietPlansListByPatient(nutritionist.getEmail(), patientEmail);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/get-diet-plan-by-id/{dietPlanId}")
    public ResponseEntity<DietPlanDTO> getDietPlanById(@PathVariable Long dietPlanId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        DietPlanDTO dietPlanDTO = nutriService.getDietPlanById(nutritionist.getEmail(), dietPlanId);
        return new ResponseEntity<>(dietPlanDTO, HttpStatus.OK);
    }





    @PutMapping("/patient/update")
    @Operation(summary = "Updates Patient Info")
    public ResponseEntity<String> updatePatient(@RequestBody PatientRegistrationByNutritionistDTO registrationDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        nutriService.updatePatient(registrationDTO, nutritionist.getEmail());
        return new ResponseEntity<>("Actualizado con éxito", HttpStatus.CREATED);
    }

    @PutMapping("/appointment/confirm/{appointmentId}")
    @Operation(summary = "Confirms a solicited appointment by patient")
    public ResponseEntity<String> confirmAppointment(@PathVariable Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        nutriService.confirmAppointment(appointmentId, nutritionist);
        return new ResponseEntity<>("Cita confirmada", HttpStatus.OK);
    }

    @DeleteMapping("/appointment/delete/{appointmentId}")
    @Operation(summary = "Cancels and deletes an appointment")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User nutritionist = (User) authentication.getPrincipal();
        nutriService.deleteAppointment(appointmentId, nutritionist);
        return new ResponseEntity<>("Cita eliminada", HttpStatus.OK);
    }

    @DeleteMapping("/patient/{email}")
    @Operation(summary = "Deletes a patient")
    public ResponseEntity<String> deletePatient(@PathVariable String email){
        if (!userService.existsById(email))
            throw new UsernameNotFoundException("El paciente no existe");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (userService.validateParentEmailForUser(email, user.getEmail())){
            userService.deletePatient(email);
            return new ResponseEntity<>("Paciente Eliminado", HttpStatus.OK);
        }
        else throw new BadCredentialsException("No tienes permisos sobre este usuario");
    }


}
