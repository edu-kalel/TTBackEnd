package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.DietPlan;
import escom.ttbackend.model.entities.Meal;
import escom.ttbackend.model.entities.PatientRecord;
import escom.ttbackend.model.entities.Post;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.AppointmentDTO;
import escom.ttbackend.presentation.dto.AppointmentRequest;
import escom.ttbackend.presentation.dto.DietPlanDTO;
import escom.ttbackend.presentation.dto.PatientDTO;
import escom.ttbackend.presentation.dto.PatientRecordResponse;
import escom.ttbackend.presentation.dto.PostDTO;
import escom.ttbackend.presentation.dto.SimpleUserDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.repository.AppointmentRepository;
import escom.ttbackend.repository.DietPlanRepository;
import escom.ttbackend.repository.MealRepository;
import escom.ttbackend.repository.PatientRecordRepository;
import escom.ttbackend.repository.PostRepository;
import escom.ttbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final Mapper mapper;
    private final PostRepository postRepository;
    private final DietPlanRepository dietPlanRepository;
    private final PatientRecordRepository patientRecordRepository;
    private final EmailService emailService;
    private final MealRepository mealRepository;


    public UserDTO getUserDTObyID(String email){
        return mapper.mapToUserDTO(userRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist")));
    }

    @Transactional
    public void deletePatient(String email) {
        List<Appointment> appointments = appointmentRepository.findByPatient_Email(email);
        List<Post> posts = postRepository.findByPatient_Email(email);
        List<DietPlan> dietPlans = dietPlanRepository.findByUser_Email(email);
        List<PatientRecord> patientRecords = patientRecordRepository.findByPatient_Email(email);
        if (!appointments.isEmpty())
            appointmentRepository.deleteAll(appointments);
        if (!posts.isEmpty())
            postRepository.deleteAll(posts);
        if (!patientRecords.isEmpty())
            patientRecordRepository.deleteAll(patientRecords);
        if (!dietPlans.isEmpty()){
            for (DietPlan dietPlan : dietPlans){
                List<Meal> meals = mealRepository.findByDietPlan_IdDietPlan(dietPlan.getIdDietPlan());
                if (!meals.isEmpty()){
                    mealRepository.deleteAll(meals);
                }
            }
            dietPlanRepository.deleteAll(dietPlans);
        }
        userRepository.deleteById(email);
    }

    public boolean existsById(String email) {
        return userRepository.existsById(email);
    }

    public boolean validateParentEmailForUser(String userEmail, String parentEmail) {
        User user = userRepository.findById(userEmail).orElse(null);//TODO IMPROOOOVE FFS
        if (user != null) {
            return user.getParent().getEmail().equals(parentEmail);
        }
        return false;
    }
    public boolean isFromClinic(String clinic, String userEmail) {
        User user = userRepository.findById(userEmail).orElseThrow();
        return user.getClinic().equals(clinic);
    }
    public List<UserDTO> getUsersByParentEmail(String parentEmail) {
        List<User> users = userRepository.findByParent_Email(parentEmail);
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(mapper.mapToUserDTO(user));
        }
        return userDTOS;
    }

    public boolean validateGrandpaEmailForUser(String userEmail, String grandpaEmail) {
        User user = userRepository.findById(userEmail).orElse(null);//TODO IMPROOOOVE FFS
        if (user != null) {
            return user.getParent().getParent().getEmail().equals(grandpaEmail);
        }
        return false;
    }
    public List<SimpleUserDTO> getSimpleUserDTOsByParentEmail(String parentEmail) {
        List<User> users = userRepository.findByParent_Email(parentEmail);
        List<SimpleUserDTO> simpleUserDTOS = new ArrayList<>();
        for (User user : users) {
            simpleUserDTOS.add(mapper.mapToSimpleUserDTO(user));
        }
        return simpleUserDTOS;
    }

    public AppointmentDTO scheduleAppointment(AppointmentRequest appointmentRequest, String email, AppointmentStatus appointmentStatus) {
        User nutritionist = userRepository.findById(email).orElseThrow(() -> new UsernameNotFoundException("Nutritionist does not exist"));
        User patient = userRepository.findById(appointmentRequest.getPatient_email()).orElseThrow(() -> new UsernameNotFoundException("Patient does not exist"));
        if (validateParentEmailForUser(patient.getEmail(), nutritionist.getEmail())) {

            checkOverlappingAppointments(nutritionist, appointmentRequest.getStarting_time(), appointmentRequest.getEnding_time());

            var appointment = Appointment.builder()
                    .nutritionist(nutritionist)
                    .startingTime(appointmentRequest.getStarting_time())
                    .patient(patient)
                    .endingTime(appointmentRequest.getEnding_time())
                    .appointmentStatus(appointmentStatus)
                    .build();
            AppointmentDTO appointmentDTO = mapper.mapToAppointmentDTO(appointmentRepository.save(appointment));

            if (appointmentStatus.equals(AppointmentStatus.CONFIRMED))
                emailService.sendsAppointementCreationMessage(nutritionist, patient, appointment);

            log.info("Cita agendada -> {}", appointmentDTO);
            return appointmentDTO;
        }
        else throw new BadCredentialsException("No permissions over this user");
    }

    public void checkOverlappingAppointments(User nutritionist, LocalDateTime startingTime, LocalDateTime endingTime){
        // Check for existing appointments that overlap with the new appointment
        List<Appointment> existingAppointments = appointmentRepository.findByNutritionistAndTimeOverlapWithStatus(
                nutritionist, startingTime, endingTime, AppointmentStatus.CONFIRMED);

        // Check if there are any overlapping appointments
        if (!existingAppointments.isEmpty()) {
            throw new EntityExistsException("The new appointment overlaps with an existing appointment.");
        }
    }

    public List<PostDTO> getPostsByPatient(String email) {
        List<Post> posts = postRepository.findByPatient_Email(email);
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            postDTOS.add(mapper.mapToPostDTO(post));
        }
        return postDTOS;
    }

    public List<PatientRecordResponse> getPatientRecords(String patientEmail) {
//        List<PatientRecord> patientRecords = patientRecordRepository.findByPatient_EmailOrderByDateDesc(patientEmail);
        return patientRecordRepository.findByPatient_EmailOrderByDateDesc(patientEmail)
                .stream()
                .map(mapper::mapToPatientRecordDTO)
                .collect(Collectors.toList());
    }

    public int calculateAge(LocalDate dateOfBirth) {
        Period period = Period.between(dateOfBirth, LocalDate.now());
        return period.getYears();
    }

    public PatientDTO getPatientDTObyID(String email) {
        return mapper.mapToPatientDTO(userRepository.findById(email)
          .orElseThrow(() -> new UsernameNotFoundException("User does not exist")));
    }

  public DietPlanDTO getLatestDietPlan(String email) {
        DietPlan dietPlan = dietPlanRepository.findFirstByUser_EmailOrderByDateDesc(email);
        DietPlanDTO dietPlanDTO = mapper.mapToDietPlanDTO(dietPlan);
        return dietPlanDTO;
  }
}
