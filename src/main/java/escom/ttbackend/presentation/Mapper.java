package escom.ttbackend.presentation;

import escom.ttbackend.model.entities.*;
import escom.ttbackend.presentation.dto.*;
import escom.ttbackend.presentation.dto.calculation.DietResponseBody;
import escom.ttbackend.presentation.dto.calculation.PortionsDTO;
import escom.ttbackend.repository.PatientRecordRepository;
import escom.ttbackend.service.implementation.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final PatientRecordRepository patientRecordRepository;

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
                appointment.getId_appointment(),
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

    public DietPlanDTO mapToDietPlanDTO(DietPlan latestDietPlan) {
        return new DietPlanDTO(
                latestDietPlan.getUser().getEmail(),
                latestDietPlan.getGoal(),
                latestDietPlan.getKcal(),
                latestDietPlan.getComment(),
                latestDietPlan.getMeals()
        );
    }

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
                appointment.getId_appointment(),
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
}
