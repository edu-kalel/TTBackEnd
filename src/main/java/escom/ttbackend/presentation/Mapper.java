package escom.ttbackend.presentation;

import escom.ttbackend.model.entities.*;
import escom.ttbackend.presentation.dto.*;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

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
                patientRecord.getPatientWeight(),
                patientRecord.getComment()
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
//    public User mapToNutritionist(RegistrationDTO registrationDTO){
//        User secretary = userService.getByEmail(registrationDTO.getParent_email());
//        var user = User.builder()
//                .email(registrationDTO.getEmail())
//                .first_name(registrationDTO.getFirst_name())
//                .last_name(registrationDTO.getLast_name())
//                .date_of_birth(registrationDTO.getDate_of_birth())
//                .phone(registrationDTO.getPhone())
//                .
//    }
}
