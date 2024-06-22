package escom.ttbackend.service.implementation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import escom.ttbackend.model.entities.Aliment;
import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.DietPlan;
import escom.ttbackend.model.entities.Meal;
import escom.ttbackend.model.entities.PatientRecord;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.ActivityLevel;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.model.enums.Sex;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.AlimentDTO;
import escom.ttbackend.presentation.dto.AppointmentDTO;
import escom.ttbackend.presentation.dto.BigPatientInfoDTO;
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
import jakarta.persistence.EntityExistsException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.math.Fraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class NutriServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private AppointmentRepository appointmentRepository;

  @Mock
  private Mapper mapper;

  @Mock
  private UserService userService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private PostRepository postRepository;

  @Mock
  private DietPlanRepository dietPlanRepository;

  @Mock
  private PatientRecordRepository patientRecordRepository;

  @Mock
  private EmailService emailService;

  @Mock
  private DietPlanCalculationsService dietPlanCalculationsService;

  @Mock
  private AlimentRepository alimentRepository;

  @Mock
  private AlimentGroupRepository alimentGroupRepository;

  @Mock
  private MealRepository mealRepository;

  @InjectMocks
  private NutriService nutriService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

//  @Test
//  void getAppointmentsByStatusTest() {
//    String email = "nutritionist@example.com";
//    AppointmentStatus status = AppointmentStatus.CONFIRMED;
//    List<Appointment> appointments = new ArrayList<>();
//    appointments.add(new Appointment());
//
//    when(appointmentRepository.findByNutritionist_EmailAndAppointmentStatus(email, status)).thenReturn(appointments);
//    when(mapper.mapToSimpleAppointmentDTO(any(Appointment.class))).thenReturn(new SimpleAppointmentDTO());
//
//    List<SimpleAppointmentDTO> result = nutriService.getAppointmentsByStatus(email, status);
//
//    assertNotNull(result);
//    assertEquals(1, result.size());
//  }

//  @Test
//  void getAllAppointmentsTest() {
//    String email = "nutritionist@example.com";
//    List<Appointment> appointments = new ArrayList<>();
//    appointments.add(new Appointment());
//
//    when(appointmentRepository.findByNutritionist_Email(email)).thenReturn(appointments);
//    when(mapper.mapToAppointmentDTO(any(Appointment.class))).thenReturn(new AppointmentDTO());
//
//    List<AppointmentDTO> result = nutriService.getAllApointments(email);
//
//    assertNotNull(result);
//    assertEquals(1, result.size());
//  }

  @Test
  void registerNewPatientTest() {
    PatientRegistrationByNutritionistDTO request = new PatientRegistrationByNutritionistDTO();
    request.setEmail("newpatient@example.com");
    request.setPassword("password");
    User parentUser = new User();

    when(userRepository.existsById(request.getEmail())).thenReturn(false);
    when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

    assertDoesNotThrow(() -> nutriService.registerNewPatient(request, parentUser));
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void registerNewPatientAlreadyExistsTest() {
    PatientRegistrationByNutritionistDTO request = new PatientRegistrationByNutritionistDTO();
    request.setEmail("existingpatient@example.com");

    when(userRepository.existsById(request.getEmail())).thenReturn(true);

    assertThrows(EntityExistsException.class, () -> nutriService.registerNewPatient(request, new User()));
  }

  @Test
  void updatePatientTest() {
    PatientRegistrationByNutritionistDTO request = new PatientRegistrationByNutritionistDTO();
    request.setEmail("patient@example.com");
    request.setPassword("");

    User existingUser = new User();
    existingUser.setPassword("oldPassword");

    when(userRepository.findById(request.getEmail())).thenReturn(Optional.of(existingUser));
    when(userService.validateParentEmailForUser(request.getEmail(), "nutritionist@example.com")).thenReturn(true);

    assertDoesNotThrow(() -> nutriService.updatePatient(request, "nutritionist@example.com"));
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void confirmAppointmentTest() {
    Long appointmentId = 1L;
    User nutritionist = new User();
    nutritionist.setEmail("nutritionist@example.com");

    Appointment appointment = new Appointment();
    appointment.setNutritionist(nutritionist);
    appointment.setStartingTime(LocalDateTime.now().plusDays(1));
    appointment.setEndingTime(LocalDateTime.now().plusDays(1).plusHours(1));

    when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
    doNothing().when(userService).checkOverlappingAppointments(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class));
    doNothing().when(emailService).sendsAppointementConfirmationMessage(any(User.class), any(User.class), any(Appointment.class));

    assertDoesNotThrow(() -> nutriService.confirmAppointment(appointmentId, nutritionist));
    assertEquals(AppointmentStatus.CONFIRMED, appointment.getAppointmentStatus());
    verify(appointmentRepository, times(1)).save(any(Appointment.class));
  }

  @Test
  void deleteAppointmentTest() {
    Long appointmentId = 1L;
    User nutritionist = new User();
    nutritionist.setEmail("nutritionist@example.com");

    Appointment appointment = new Appointment();
    appointment.setNutritionist(nutritionist);

    when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

    assertDoesNotThrow(() -> nutriService.deleteAppointment(appointmentId, nutritionist));
    verify(appointmentRepository, times(1)).deleteById(appointmentId);
  }

//  @Test
//  void getTodayConfirmedAppointmentsTest() {
//    String nutritionistEmail = "nutritionist@example.com";
//    LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
//
//    List<Appointment> appointments = new ArrayList<>();
//    appointments.add(new Appointment());
//
//    when(appointmentRepository.findByNutritionist_EmailAndStartingTimeBetweenAndAppointmentStatusOrderByStartingTimeAsc(
//      nutritionistEmail, startOfDay, endOfDay, AppointmentStatus.CONFIRMED)).thenReturn(appointments);
//    when(mapper.mapToSimpleAppointmentDTO(any(Appointment.class))).thenReturn(new SimpleAppointmentDTO());
//
//    List<SimpleAppointmentDTO> result = nutriService.getTodayConfirmedAppointments(nutritionistEmail);
//
//    assertNotNull(result);
//    assertEquals(1, result.size());
//  }

//  @Test
//  void addPatientRecordTest() {
//    PatientRecordRequest request = new PatientRecordRequest();
//    request.setPatientEmail("patient@example.com");
//    request.setPatientHeight(190);
//    request.setPatientWeight(80);
//    request.setActivityLevel(ActivityLevel.SEDENTARY);
//
//    User patient = new User();
//    patient.setEmail("patient@example.com");
//    patient.setSex(Sex.MASC);
//    patient.setDate_of_birth(LocalDate.now().minusYears(25));
//
//    when(userRepository.findById(request.getPatientEmail())).thenReturn(Optional.of(patient));
//    when(userService.validateParentEmailForUser(request.getPatientEmail(), "nutritionist@example.com")).thenReturn(true);
//    when(patientRecordRepository.save(any(PatientRecord.class))).thenReturn(new PatientRecord());
//    when(dietPlanCalculationsService.calculateGER(any(User.class), any(PatientRecord.class), anyInt())).thenReturn(1962);
//    when(dietPlanCalculationsService.calculateGET(anyInt(), any(ActivityLevel.class), any(Sex.class))).thenReturn(2354);
//
//    CaloriesCalculationDTO result = nutriService.addPatientRecord(request, "nutritionist@example.com");
//
//    assertNotNull(result);
//    assertEquals(1962, result.getGer());
//    assertEquals(2354, result.getGet());
//  }

  @Test
  void calculatePortionsTest() throws IOException {
    DietRequestBody request = new DietRequestBody();
    String nutritionistEmail = "nutritionist@example.com";
    String patientEmail = "patient@example.com";

    User patient = new User();
    patient.setEmail(patientEmail);

    when(userRepository.findById(patientEmail)).thenReturn(Optional.of(patient));
    when(userService.validateParentEmailForUser(patientEmail, nutritionistEmail)).thenReturn(true);
    when(dietPlanCalculationsService.getPortions(request)).thenReturn(new PortionsDTO());

    PortionsDTO result = nutriService.calculatePortions(request, nutritionistEmail, patientEmail);

    assertNotNull(result);
  }

//  @Test
//  void getBigInfoTest() {
//    String patientEmail = "patient@example.com";
//    String nutritionistEmail = "nutritionist@example.com";
//
//    User patient = new User();
//    patient.setEmail(patientEmail);
//    patient.setDate_of_birth(LocalDate.now().minusYears(30));
//
//    when(userRepository.findById(patientEmail)).thenReturn(Optional.of(patient));
//    when(userService.validateParentEmailForUser(patientEmail, nutritionistEmail)).thenReturn(true);
//    when(userService.calculateAge(patient.getDate_of_birth())).thenReturn(30);
//    when(mapper.mapToBigPatientInfoDTO(any(User.class), anyInt())).thenReturn(new BigPatientInfoDTO());
//
//    BigPatientInfoDTO result = nutriService.getBigInfo(patientEmail, nutritionistEmail);
//
//    assertNotNull(result);
//  }

  @Test
  void createNewDietPlanEntityTest() {
    String nutritionistEmail = "nutritionist@example.com";
    String patientEmail = "patient@example.com";

    User patient = new User();
    patient.setEmail(patientEmail);

    when(userRepository.findById(patientEmail)).thenReturn(Optional.of(patient));
    when(userService.validateParentEmailForUser(patientEmail, nutritionistEmail)).thenReturn(true);
    when(dietPlanRepository.save(any(DietPlan.class))).thenReturn(new DietPlan());

    assertDoesNotThrow(() -> nutriService.createNewDietPlanEntity(nutritionistEmail, patientEmail));
    verify(dietPlanRepository, times(1)).save(any(DietPlan.class));
  }

//  @Test
//  void addMealsToDietPlanTest() {
//    MealsToAddDTO request = new MealsToAddDTO();
//    request.setDietPlanId(1L);
//    List<IndividualFoodDTO> meals = new ArrayList<>();
//    IndividualFoodDTO foodDTO = new IndividualFoodDTO();
//    foodDTO.setAlimentId(1L);
//    foodDTO.setQuantity("2");
//    meals.add(foodDTO);
//    request.setMeals(meals);
//
//    DietPlan dietPlan = new DietPlan();
//    Aliment aliment = new Aliment();
//    aliment.setQuantity(Fraction.ONE);
//    aliment.setUnit("g");
//
//    when(dietPlanRepository.findById(request.getDietPlanId())).thenReturn(Optional.of(dietPlan));
//    when(alimentRepository.findById(foodDTO.getAlimentId())).thenReturn(Optional.of(aliment));
//    when(mealRepository.save(any(Meal.class))).thenReturn(new Meal());
//
//    assertDoesNotThrow(() -> nutriService.addMealsToDietPlan(request));
//    verify(mealRepository, times(1)).save(any(Meal.class));
//  }

//  @Test
//  void finishDietPlanTest() {
//    FinishDietPlanDTO request = new FinishDietPlanDTO();
//    request.setId(1L);
//    request.setGoal("Lose Weight");
//    request.setComment("Follow this plan");
//
//    DietPlan dietPlan = new DietPlan();
//
//    when(dietPlanRepository.findById(request.getId())).thenReturn(Optional.of(dietPlan));
//
//    assertDoesNotThrow(() -> nutriService.finishDietPlan(request));
//    verify(dietPlanRepository, times(1)).save(any(DietPlan.class));
//  }

//  @Test
//  void getAllAlimentsTest() {
//    List<Aliment> alimentList = new ArrayList<>();
//    alimentList.add(new Aliment());
//
//    when(alimentRepository.findAll()).thenReturn(alimentList);
//    when(mapper.mapToAlimentDTO(any(Aliment.class))).thenReturn(new AlimentDTO());
//
//    List<AlimentDTO> result = nutriService.getAllAliments();
//
//    assertNotNull(result);
//    assertEquals(1, result.size());
//  }

//  @Test
//  void getDietPlansListByPatientTest() {
//    String nutritionistEmail = "nutritionist@example.com";
//    String patientEmail = "patient@example.com";
//
//    User patient = new User();
//    patient.setEmail(patientEmail);
//
//    List<DietPlan> dietPlanList = new ArrayList<>();
//    dietPlanList.add(new DietPlan());
//
//    when(userRepository.findById(patientEmail)).thenReturn(Optional.of(patient));
//    when(userService.validateParentEmailForUser(patientEmail, nutritionistEmail)).thenReturn(true);
//    when(dietPlanRepository.findAllByUser_EmailOrderByDateDesc(patientEmail)).thenReturn(dietPlanList);
//    when(mapper.mapToSimpleDietPlanDTO(any(DietPlan.class))).thenReturn(new SimpleDietPlanDTO());
//
//    List<SimpleDietPlanDTO> result = nutriService.getDietPlansListByPatient(nutritionistEmail, patientEmail);
//
//    assertNotNull(result);
//    assertEquals(1, result.size());
//  }

  @Test
  void getDietPlanByIdTest() {
    String nutritionistEmail = "nutritionist@example.com";
    Long dietPlanId = 1L;

    DietPlan dietPlan = new DietPlan();

    when(dietPlanRepository.findById(dietPlanId)).thenReturn(Optional.of(dietPlan));
    when(mapper.mapToDietPlanDTO(any(DietPlan.class))).thenReturn(new DietPlanDTO());

    DietPlanDTO result = nutriService.getDietPlanById(nutritionistEmail, dietPlanId);

    assertNotNull(result);
  }
}
