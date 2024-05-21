package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.DietPlan;
import escom.ttbackend.model.entities.PatientRecord;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.model.enums.Role;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.*;
import escom.ttbackend.repository.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutriService{
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final Mapper mapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final DietPlanRepository dietPlanRepository;
    private final PatientRecordRepository patientRecordRepository;
    private final EmailService emailService;

    /*public AppointmentDTO scheduleAppointment(AppointmentRequest appointmentRequest, String email, AppointmentStatus appointmentStatus) {
        User nutritionist = userRepository.findById(email).orElseThrow(() -> new UsernameNotFoundException("Nutritionist does not exist"));
        User patient = userRepository.findById(appointmentRequest.getPatient_email()).orElseThrow(() -> new UsernameNotFoundException("Patient does not exist"));
        if (userService.validateParentEmailForUser(patient.getEmail(), nutritionist.getEmail())) {

            // Check for existing appointments that overlap with the new appointment
            List<Appointment> existingAppointments = appointmentRepository.findByNutritionistAndTimeOverlap(
                    nutritionist, appointmentRequest.getStarting_time(), appointmentRequest.getEnding_time());

            // Check if there are any overlapping appointments
            if (!existingAppointments.isEmpty()) {
                throw new EntityExistsException("The new appointment overlaps with an existing appointment.");
            }

            var appointment = Appointment.builder()
                    .nutritionist(nutritionist)
                    .starting_time(appointmentRequest.getStarting_time())
                    .patient(patient)
                    .ending_time(appointmentRequest.getEnding_time())
                    .appointmentStatus(appointmentStatus)
                    .build();
            return mapper.mapToAppointmentDTO(appointmentRepository.save(appointment));
        }
        else throw new BadCredentialsException("No permissions over this user");
    }*/

    public List<AppointmentDTO> getAppointmentsByStatus(String email, AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByNutritionist_EmailAndAppointmentStatus(email, status);
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDTOS.add(mapper.mapToAppointmentDTO(appointment));
        }
        return appointmentDTOS;
    }

//    public void deleteAppointment(User nutritionist, LocalDateTime startingTime) {
//        AppointmentId appointmentId = new AppointmentId(nutritionist, startingTime);
//        appointmentRepository.deleteById(appointmentId);
//    }


    public List<AppointmentDTO> getAllApointments(String email) {
        List<Appointment> appointments = appointmentRepository.findByNutritionist_Email(email);
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDTOS.add(mapper.mapToAppointmentDTO(appointment));
        }
        return appointmentDTOS  ;
    }
    public UserDTO registerNewPatient(PatientRegistrationByNutritionistDTO request, User parentUser) {
        if (userRepository.existsById(request.getEmail())){
            throw new EntityExistsException("User Already Exists");
        }
        else{
            var user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.PATIENT)
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.isSex())
                    .parent(parentUser)
                    .ailments(request.getAilments())
                    .clinic(parentUser.getClinic())
                    .build();
            return mapper.mapToUserDTO(userRepository.save(user));
        }
    }

    public UserDTO updatePatient(PatientRegistrationByNutritionistDTO request) {    //TODO maybe refactor this, like -> adminService -> updateUser
        var user = userRepository.findById(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        if(request.getPassword().isBlank()) {
            user = User.builder().password(
//                            userRepository.findByEmail(request.getEmail()).get().getPassword()
                            user.getPassword()
                    )
                    .clinic(user.getClinic())
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .role(Role.PATIENT)
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.isSex())
                    .ailments(request.getAilments())
                    .parent(user.getParent())
                    .build();
        } else {
            user = User.builder()
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.PATIENT)
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.isSex())
                    .ailments(request.getAilments())
                    .parent(user.getParent())
                    .clinic(user.getClinic())
                    .build();
        }

        return mapper.mapToUserDTO(userRepository.save(user));
    }

//    public List<PostDTO> getPostsByPatient(String email) {
//        List<Post> posts = postRepository.findByPatient_Email(email);
//        List<PostDTO> postDTOS = new ArrayList<>();
//        for (Post post : posts) {
//            postDTOS.add(mapper.mapToPostDTO(post));
//        }
//        return postDTOS  ;
//    }

    public void confirmAppointment(Long appointmentId, User nutritionist) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("Appointment does not exist"));
        if (appointment.getNutritionist().getEmail().equals(nutritionist.getEmail())){
            userService.checkOverlappingAppointments(nutritionist, appointment.getStartingTime(), appointment.getEndingTime());
            appointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
            appointmentRepository.save(appointment);
            User patient = appointment.getPatient();
            emailService.sendsAppointementConfirmationMessage(nutritionist, patient, appointment);
        }
    }

    public void deleteAppointment(Long appointmentId, User nutritionist) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("Appointment does not exist"));
        if (appointment.getNutritionist().getEmail().equals(nutritionist.getEmail())){
            appointmentRepository.deleteById(appointmentId);
        }
    }

    public List<AppointmentDTO> getTodayConfirmedAppointments(String nutritionistEmail) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return appointmentRepository.findByNutritionist_EmailAndStartingTimeBetweenAndAppointmentStatusOrderByStartingTimeAsc(
                nutritionistEmail, startOfDay, endOfDay, AppointmentStatus.CONFIRMED)
                .stream()
                .map(mapper::mapToAppointmentDTO)
                .collect(Collectors.toList());
    }

    public String addNewDietPlan(DietPlanDTO request, User parent) {
        var user = userRepository.findById(request.getUser_email())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        if (!userService.validateParentEmailForUser(request.getUser_email(), parent.getEmail())&&!request.getUser_email().equals(parent.getEmail()))
            throw new BadCredentialsException("The user you are trying to assign a Diet Plan is not yours to conquer");
        DietPlan existingDietPlan = dietPlanRepository.findByUser_Email(user.getEmail());
        if (existingDietPlan == null) {
            DietPlan dietPlan = DietPlan.builder()
                    .user(user)
                    .goal(request.getGoal())
                    .kcal(request.getKcal())
                    .date(LocalDate.now())
                    .comment(request.getComment())
                    .meals(request.getMeals())
                    .build();
            dietPlanRepository.save(dietPlan);
            return "Diet plan created successfully";
        } else {
            existingDietPlan.setGoal(request.getGoal());
            existingDietPlan.setKcal(request.getKcal());
            existingDietPlan.setDate(LocalDate.now());
            existingDietPlan.setComment(request.getComment());
            existingDietPlan.setMeals(request.getMeals());
            dietPlanRepository.save(existingDietPlan);
            return "Diet plan updated successfully";
        }
//        var dietPlan = DietPlan.builder()
//                .user(user)
//                .goal(request.getGoal())
//                .kcal(request.getKcal())
//                .date(LocalDate.now())
//                .comment(request.getComment())
//                .meals(request.getMeals())
//                .build();
//        dietPlanRepository.save(dietPlan);
    }

    public String addPatientRecord(PatientRecordRequest request, String nutritionist_email) {
        var patient = userRepository.findById(request.getPatientEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Patient " + request.getPatientEmail() + " does not exist"));
        if (!userService.validateParentEmailForUser(request.getPatientEmail(), nutritionist_email))
            throw new BadCredentialsException("No permissions over patient " + request.getPatientEmail());
        var patientRecord = PatientRecord.builder()
                .patient(patient)
                .patientHeight(request.getPatientHeight())
                .patientWeight(request.getPatientWeight())
                .comment(request.getComment())
                .date(LocalDate.now())
                .build();
        patientRecordRepository.save(patientRecord);
        return "Patient record created successfully";
    }
}
