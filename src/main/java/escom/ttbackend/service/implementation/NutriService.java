package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Aliment;
import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.DietPlan;
import escom.ttbackend.model.entities.Meal;
import escom.ttbackend.model.entities.PatientRecord;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.model.enums.Role;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.AlimentDTO;
import escom.ttbackend.presentation.dto.AppointmentDTO;
import escom.ttbackend.presentation.dto.BigPatientInfoDTO;
import escom.ttbackend.presentation.dto.CardDTO;
import escom.ttbackend.presentation.dto.DietPlanDTO;
import escom.ttbackend.presentation.dto.FinishDietPlanDTO;
import escom.ttbackend.presentation.dto.IndividualFoodDTO;
import escom.ttbackend.presentation.dto.MealsToAddDTO;
import escom.ttbackend.presentation.dto.PatientRecordRequest;
import escom.ttbackend.presentation.dto.PatientRegistrationByNutritionistDTO;
import escom.ttbackend.presentation.dto.SimpleAppointmentDTO;
import escom.ttbackend.presentation.dto.SimpleDietPlanDTO;
import escom.ttbackend.presentation.dto.calculation.CaloriesCalculationDTO;
import escom.ttbackend.presentation.dto.calculation.DietRequestBody;
import escom.ttbackend.presentation.dto.calculation.PortionsDTO;
import escom.ttbackend.repository.AlimentGroupRepository;
import escom.ttbackend.repository.AlimentRepository;
import escom.ttbackend.repository.AppointmentRepository;
import escom.ttbackend.repository.DietPlanRepository;
import escom.ttbackend.repository.MealRepository;
import escom.ttbackend.repository.PatientRecordRepository;
import escom.ttbackend.repository.PostRepository;
import escom.ttbackend.repository.UserRepository;
import escom.ttbackend.utils.CardGenerator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.Fraction;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NutriService{
    private final CardGenerator cardGenerator;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final Mapper mapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final DietPlanRepository dietPlanRepository;
    private final PatientRecordRepository patientRecordRepository;
    private final EmailService emailService;
    private final DietPlanCalculationsService dietPlanCalculationsService;
    private final AlimentRepository alimentRepository;
    private final AlimentGroupRepository alimentGroupRepository;
    private final MealRepository mealRepository;


    public List<SimpleAppointmentDTO> getAppointmentsByStatus(String email, AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByNutritionist_EmailAndAppointmentStatus(email, status);
        List<SimpleAppointmentDTO> appointmentDTOS = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDTOS.add(mapper.mapToSimpleAppointmentDTO(appointment));
        }
        return appointmentDTOS;
    }



    public List<AppointmentDTO> getAllApointments(String email) {
        List<Appointment> appointments = appointmentRepository.findByNutritionist_Email(email);
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDTOS.add(mapper.mapToAppointmentDTO(appointment));
        }
        return appointmentDTOS  ;
    }
    public void registerNewPatient(PatientRegistrationByNutritionistDTO request, User parentUser) {
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
                    .sex(request.getSex())
                    .parent(parentUser)
                    .ailments(request.getAilments())
                    .clinic(parentUser.getClinic())
                    .build();
            userRepository.save(user);
//            return mapper.mapToUserDTO(userRepository.save(user));
        }
    }

    public void updatePatient(PatientRegistrationByNutritionistDTO request, String nutritionistEmail) {    //TODO maybe refactor this, like -> adminService -> updateUser
        var user = userRepository.findById(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Patient does not exist"));
        if (!userService.validateParentEmailForUser(request.getEmail(), nutritionistEmail))
            throw new BadCredentialsException("No permissions over patient " + request.getEmail());
        if(request.getPassword().isBlank()) {
            user = User.builder().password(
                            user.getPassword()
                    )
                    .clinic(user.getClinic())
                    .email(request.getEmail())
                    .first_name(request.getFirst_name())
                    .last_name(request.getLast_name())
                    .phone(request.getPhone())
                    .role(Role.PATIENT)
                    .date_of_birth(request.getDate_of_birth())
                    .sex(request.getSex())
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
                    .sex(request.getSex())
                    .ailments(request.getAilments())
                    .parent(user.getParent())
                    .clinic(user.getClinic())
                    .build();
        }
        userRepository.save(user);
//        mapper.mapToUserDTO(userRepository.save(user));
    }


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

    public List<SimpleAppointmentDTO> getTodayConfirmedAppointments(String nutritionistEmail) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return appointmentRepository.findByNutritionist_EmailAndStartingTimeBetweenAndAppointmentStatusOrderByStartingTimeAsc(
                nutritionistEmail, startOfDay, endOfDay, AppointmentStatus.CONFIRMED)
                .stream()
                .map(mapper::mapToSimpleAppointmentDTO)
                .collect(Collectors.toList());
    }

    public CaloriesCalculationDTO addPatientRecord(PatientRecordRequest request, String nutritionist_email) {
        var patient = userRepository.findById(request.getPatientEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Patient " + request.getPatientEmail() + " does not exist"));
        if (!userService.validateParentEmailForUser(request.getPatientEmail(), nutritionist_email))
            throw new BadCredentialsException("No permissions over patient " + request.getPatientEmail());
        var patientRecord = PatientRecord.builder()
                .patient(patient)
                .patientHeight(request.getPatientHeight())
                .patientWeight(request.getPatientWeight())
                .date(LocalDate.now())
                .build();
        patientRecordRepository.save(patientRecord);
        CaloriesCalculationDTO calculation = new CaloriesCalculationDTO();
        int ger = (dietPlanCalculationsService.calculateGER(patient, patientRecordRepository.findFirstByPatient_EmailOrderByDateDesc(patient.getEmail()),userService.calculateAge(patient.getDate_of_birth())));
        calculation.setGer(ger);
        calculation.setGet(dietPlanCalculationsService.calculateGET(ger, request.getActivityLevel(), patient.getSex()));
        return calculation;
    }


    public PortionsDTO calculatePortions(DietRequestBody request, String nutritionistEmail, String patientEmail) throws IOException {
        log.info("enters calculate portions in nutri service");
        var patient = userRepository.findById(patientEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Patient " + patientEmail + " does not exist"));
        if (!userService.validateParentEmailForUser(patientEmail, nutritionistEmail))
            throw new BadCredentialsException("No permissions over patient " + patientEmail);
        return dietPlanCalculationsService.getPortions(request);
    }

    public BigPatientInfoDTO getBigInfo(String patientEmail, String nutritionistEmail) {
        var patient = userRepository.findById(patientEmail)
                .orElseThrow(() -> new UsernameNotFoundException("El paciente con email " + patientEmail + " no existe"));
        if (!userService.validateParentEmailForUser(patientEmail, nutritionistEmail))
            throw new BadCredentialsException("No permisos sobre el paciente: " + patientEmail);
        int age = userService.calculateAge(patient.getDate_of_birth());
        return mapper.mapToBigPatientInfoDTO(patient, age);
    }

    public Long createNewDietPlanEntity(String nutritionistEmail, String patientEmail) {
        var patient = userRepository.findById(patientEmail)
          .orElseThrow(() -> new UsernameNotFoundException("El paciente con email " + patientEmail + " no existe"));
        if (!userService.validateParentEmailForUser(patientEmail, nutritionistEmail))
            throw new BadCredentialsException("No permisos sobre el paciente: " + patientEmail);
        DietPlan dietPlan = DietPlan.builder()
          .user(patient)
          .build();
        return dietPlanRepository.save(dietPlan).getIdDietPlan();
    }

    public void addMealsToDietPlan(MealsToAddDTO request) {
        var dietPlan = dietPlanRepository.findById(request.getDietPlanId()).get();
        for (IndividualFoodDTO foodDTO : request.getMeals()){
            var aliment = alimentRepository.findById(foodDTO.getAlimentId()).get();
            Fraction multiplier = Fraction.getFraction(foodDTO.getQuantity());
            Fraction quantityToEat = aliment.getQuantity().multiplyBy(multiplier);
            var meal = Meal.builder()
              .mealTime(request.getMealTime())
              .dietOption(request.getDietOption())
              .quantity(quantityToEat)
              .unit(aliment.getUnit())
              .aliment(aliment)
              .dietPlan(dietPlan)
              .build();
            mealRepository.save(meal);
            log.info("Saved meal -> {}", meal);
        }
    }

    public void finishDietPlan(FinishDietPlanDTO request) {
        var dietPlan = dietPlanRepository.findById(request.getId()).get();
        dietPlan.setGoal(request.getGoal());
        dietPlan.setComment(request.getComment());
        dietPlan.setDate(LocalDate.now());
        dietPlanRepository.save(dietPlan);
    }

    public List<AlimentDTO> getAllAliments() {
        List<Aliment> alimentList = alimentRepository.findAll();
        List<AlimentDTO> dtoList = new ArrayList<>();
        for (Aliment aliment : alimentList){
            AlimentDTO alimentDTO = mapper.mapToAlimentDTO(aliment);
            dtoList.add(alimentDTO);
        }
        return dtoList;
    }

    public List<SimpleDietPlanDTO> getDietPlansListByPatient(String nutritionistEmail, String patientEmail) {
        var patient = userRepository.findById(patientEmail)
          .orElseThrow(() -> new UsernameNotFoundException("El paciente con email " + patientEmail + " no existe"));
        if (!userService.validateParentEmailForUser(patientEmail, nutritionistEmail))
            throw new BadCredentialsException("No permisos sobre el paciente: " + patientEmail);
        List<DietPlan> dietPlanList = dietPlanRepository.findAllByUser_EmailOrderByDateDesc(patientEmail);
        List<SimpleDietPlanDTO> list = new ArrayList<>();
        for (DietPlan dietPlan : dietPlanList){
            SimpleDietPlanDTO simpleDietPlanDTO = mapper.mapToSimpleDietPlanDTO(dietPlan);
            list.add(simpleDietPlanDTO);
        }
        return list;
    }

    public DietPlanDTO getDietPlanById(String nutritionistEmail, Long dietPlanId) {
        var dietPlan =  dietPlanRepository.findById(dietPlanId)
          .orElseThrow(() -> new UsernameNotFoundException("Plan de dieta no existe"));
        DietPlanDTO dietPlanDTO = mapper.mapToDietPlanDTO(dietPlan);
        return dietPlanDTO;
    }

    public List<CardDTO> getCards(String email) {
        return cardGenerator.getCards(email);
    }
}