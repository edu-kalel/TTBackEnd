package escom.ttbackend.presentation;

import escom.ttbackend.model.entities.*;
import escom.ttbackend.model.enums.DietOption;
import escom.ttbackend.model.enums.MealTime;
import escom.ttbackend.presentation.dto.*;
import escom.ttbackend.presentation.dto.calculation.DietResponseBody;
import escom.ttbackend.presentation.dto.calculation.PortionsDTO;
import escom.ttbackend.repository.MealRepository;
import escom.ttbackend.repository.PatientRecordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final PatientRecordRepository patientRecordRepository;
    private final MealRepository mealRepository;

    public UserDTO mapToUserDTO(User user){
        String parent_email;
        if (user.getParent()==null)
            parent_email="No parent";
        else
            parent_email=user.getParent().getEmail();
        return new UserDTO(
                user.getEmail(),
                user.getClinic(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getDate_of_birth(),
                user.getPhone(),
                user.getSex(),
                user.getRole(),
                parent_email
        );
    }

    public AppointmentDTO mapToAppointmentDTO(Appointment appointment){
        return new AppointmentDTO(
                appointment.getIdAppointment(),
                appointment.getNutritionist().getEmail(),
                appointment.getStartingTime(),
                appointment.getPatient().getEmail(),
                appointment.getEndingTime(),
                appointment.getAppointmentStatus()
        );
    }

    public PostDTO mapToPostDTO(Post post) {
        return new PostDTO(
                post.getPatient().getFirst_name(),
                post.getDate_time(),
                post.getContent()
        );
    }

    public SimpleUserDTO mapToSimpleUserDTO(User user) {
        return new SimpleUserDTO(
                user.getEmail(),
                user.getFirst_name(),
                user.getLast_name()
        );
    }

//    public DietPlanDTO mapToDietPlanDTO(DietPlan latestDietPlan) {
//        return new DietPlanDTO(
//                latestDietPlan.getUser().getEmail(),
//                latestDietPlan.getGoal(),
//                latestDietPlan.getKcal(),
//                latestDietPlan.getComment(),
//                latestDietPlan.getMeals()
//        );
//    }

    public PatientRecordResponse mapToPatientRecordDTO(PatientRecord patientRecord) {
        return new PatientRecordResponse(
                patientRecord.getIdPatientRecord(),
                patientRecord.getPatientHeight(),
                patientRecord.getPatientWeight()
        );
    }

    public SimpleAppointmentDTO mapToSimpleAppointmentDTO(Appointment appointment) {
        String patient_fullname = appointment.getPatient().getFirst_name()+" "+appointment.getPatient().getLast_name();
        return new SimpleAppointmentDTO(
                appointment.getIdAppointment(),
                patient_fullname,
                appointment.getStartingTime()
        );
    }

    public PortionsDTO mapToPortionsDTO(DietResponseBody received) {
        return new PortionsDTO(
                received.getVerduras(),
                received.getFrutas(),
                received.getCereales(),
                received.getCerealesConGrasa(),
                received.getLeguminosas(),
                received.getAlimentosDeOrigenAnimalMuyBajosEnGrasa(),
                received.getAlimentosDeOrigenAnimalBajosEnGrasa(),
                received.getAlimentosDeOrigenAnimalModeradosEnGrasa(),
                received.getAlimentosDeOrigenAnimalAltoContenidoDeGrasa(),
                received.getLecheDescremada(),
                received.getLecheEntera(),
                received.getGrasas(),
                received.getGrasasConProteina(),
                received.getAzucares(),
                received.getAzucaresConGrasa(),
                received.getSumaKcal(),
                received.getSumaProteinas(),
                received.getSumaLipidos(),
                received.getSumaCarbohidratos()
        );

    }

    public BigPatientInfoDTO mapToBigPatientInfoDTO(User patient, int age) {
        PatientRecord patientRecord = patientRecordRepository.findFirstByPatient_EmailOrderByDateDesc(patient.getEmail());
        return new BigPatientInfoDTO(
                patient.getFirst_name() + " " + patient.getLast_name(),
                age,
                patientRecord.getPatientWeight(),
                patientRecord.getPatientHeight()
        );
    }

    public PatientDTO mapToPatientDTO(User user) {
        String parent_email;
        if (user.getParent()==null)
            parent_email="No parent";
        else
            parent_email=user.getParent().getEmail();
        return new PatientDTO(
          user.getEmail(),
          user.getClinic(),
          user.getFirst_name(),
          user.getLast_name(),
          user.getDate_of_birth(),
          user.getPhone(),
          user.getSex(),
          user.getRole(),
          parent_email,
          user.getAilments()
        );
    }

  public AlimentDTO mapToAlimentDTO(Aliment aliment) {
        AlimentGroup group = aliment.getAlimentGroup();
        return new AlimentDTO(
          aliment.getIdAliment(),
          aliment.getName(),
          group.getName(),
          aliment.getQuantity().toProperString(),
          aliment.getUnit(),
          group.getKcal() + " kcal",
          group.getCarbs() + " gr",
          group.getFats() + " gr",
          group.getProteins() + " gr"
        );
  }

    public SimpleDietPlanDTO mapToSimpleDietPlanDTO(DietPlan dietPlan) {
        return new SimpleDietPlanDTO(
          dietPlan.getIdDietPlan(),
          dietPlan.getDate(),
          dietPlan.getGoal()
        );
    }

    public DietPlanDTO mapToDietPlanDTO(DietPlan dietPlan) {

        String breakfast1 = getMealMealString(dietPlan.getIdDietPlan(), MealTime.BREAKFAST, DietOption.OPTION1);
        String colation1_1 = getMealMealString(dietPlan.getIdDietPlan(), MealTime.COLATION1, DietOption.OPTION1);
        String lunch1 = getMealMealString(dietPlan.getIdDietPlan(), MealTime.LUNCH, DietOption.OPTION1);
        String colation2_1 = getMealMealString(dietPlan.getIdDietPlan(), MealTime.COLATION2, DietOption.OPTION1);
        String dinner1 = getMealMealString(dietPlan.getIdDietPlan(), MealTime.DINNER, DietOption.OPTION1);

        String breakfast2 = getMealMealString(dietPlan.getIdDietPlan(), MealTime.BREAKFAST, DietOption.OPTION2);
        String colation1_2 = getMealMealString(dietPlan.getIdDietPlan(), MealTime.COLATION1, DietOption.OPTION2);
        String lunch2 = getMealMealString(dietPlan.getIdDietPlan(), MealTime.LUNCH, DietOption.OPTION2);
        String colation2_2 = getMealMealString(dietPlan.getIdDietPlan(), MealTime.COLATION2, DietOption.OPTION2);
        String dinner2 = getMealMealString(dietPlan.getIdDietPlan(), MealTime.DINNER, DietOption.OPTION2);

        var dietplanDTO = DietPlanDTO.builder()
          .id(dietPlan.getIdDietPlan())
          .patientEmail(dietPlan.getUser().getEmail())
          .goal(dietPlan.getGoal())
          .date(dietPlan.getDate())
          .comment(dietPlan.getComment())
          .breakfast1(breakfast1)
          .colation1_1(colation1_1)
          .lunch1(lunch1)
          .colation2_1(colation2_1)
          .dinner1(dinner1)
          .breakfast2(breakfast2)
          .colation1_2(colation1_2)
          .lunch2(lunch2)
          .colation2_2(colation2_2)
          .dinner2(dinner2)
          .build();

        return dietplanDTO;
    }

    public String getMealMealString(Long idDietPlan, MealTime mealTime, DietOption dietOption){
        List<Meal> meals = mealRepository.findAllByDietPlan_IdDietPlanAndMealTimeAndDietOption(idDietPlan, mealTime, dietOption);
        if (meals.isEmpty())
            return "No";
        StringBuilder result = new StringBuilder();
        for (Meal meal : meals){
            result.append(meal.getQuantity().toProperString()).append(" ").append(meal.getUnit()).append(" de ")
              .append(meal.getAliment().getName()).append(". ");
        }
        return result.toString();
    }
}
