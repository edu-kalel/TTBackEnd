package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.DietPlan;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.DietPlanDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailService{
    @Autowired
    private JavaMailSender emailSender;

    private final Mapper mapper;


    public void sendsAppointementConfirmationMessage(User nutritionist, User patient, Appointment appointment) {
        MimeMessage message = emailSender.createMimeMessage();
        String minutes;
        if (appointment.getStartingTime().getMinute()<10)
            minutes = "0" + appointment.getStartingTime().getMinute();
        else
            minutes = "" + appointment.getStartingTime().getMinute();
        String text = "<h1>"
                + nutritionist.getClinic()
                + "</h1><br>Hola <b>"
                + patient.getFirst_name()
                + "</b>, la cita solicitada con tu Nutriólogo ("
                + nutritionist.getFirst_name()
                + ") ha sido aprobada para la siguiente fecha y hora: <br>"
                + "Fecha: " + appointment.getStartingTime().getDayOfMonth() + "/" + appointment.getStartingTime().getMonthValue() + "/" + appointment.getStartingTime().getYear()
                + "   Hora: " + appointment.getStartingTime().getHour() + ":" + minutes
                + "<br>Saludos";
        String subject = "Confirmación de cita - "+patient.getClinic();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("no.reply.nutrisystem@gmail.com");
            helper.setTo("emendozag1602@alumno.ipn.mx");
//            helper.setTo(patient.getEmail());
            helper.setSubject(subject);
            helper.setText(text, true); // Enable HTML
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
        emailSender.send(message);
    }

    public void sendsAppointementCreationMessage(User nutritionist, User patient, Appointment appointment) {
        MimeMessage message = emailSender.createMimeMessage();
        String minutes;
        if (appointment.getStartingTime().getMinute()<10)
            minutes = "0" + appointment.getStartingTime().getMinute();
        else
            minutes = "" + appointment.getStartingTime().getMinute();
        String text = "<h1>"
                + nutritionist.getClinic()
                + "</h1><br>Hola <b>"
                + patient.getFirst_name()
                + "</b>, se te ha agendando una cita con tu Nutriólogo ("
                + nutritionist.getFirst_name()
                + ") para la siguiente fecha y hora: <br>"
                + "Fecha: " + appointment.getStartingTime().getDayOfMonth() + "/" + appointment.getStartingTime().getMonthValue() + "/" + appointment.getStartingTime().getYear()
                + "   Hora: " + appointment.getStartingTime().getHour() + ":" + minutes
                + "<br>Saludos";
        String subject = "Nueva Cita Agendada - "+patient.getClinic();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("no.reply.nutrisystem@gmail.com");
            helper.setTo("emendozag1602@alumno.ipn.mx");
//            helper.setTo("jtirado@ipn.mx");
//            helper.setTo(patient.getEmail());
            helper.setSubject(subject);
            helper.setText(text, true); // Enable HTML
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
        emailSender.send(message);
    }

    public void sendsAppointementDeletionMessage(User nutritionist, User patient, Appointment appointment) {
        MimeMessage message = emailSender.createMimeMessage();
        String minutes;
        if (appointment.getStartingTime().getMinute()<10)
            minutes = "0" + appointment.getStartingTime().getMinute();
        else
            minutes = "" + appointment.getStartingTime().getMinute();
        String text = "<h1>"
          + nutritionist.getClinic()
          + "</h1><br>Hola <b>"
          + patient.getFirst_name()
          + "</b>, la cita con tu Nutriólogo ("
          + nutritionist.getFirst_name()
          + ") con la siguiente fecha y hora: <br>"
          + "Fecha: " + appointment.getStartingTime().getDayOfMonth() + "/" + appointment.getStartingTime().getMonthValue() + "/" + appointment.getStartingTime().getYear()
          + "   Hora: " + appointment.getStartingTime().getHour() + ":" + minutes
          + "<br> ha sido cancelada/rechazada.<br>Lamentamos las molestias. Saludos";
        String subject = "Cancelación - Rechazo de cita - "+patient.getClinic();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("no.reply.nutrisystem@gmail.com");
            helper.setTo("emendozag1602@alumno.ipn.mx");
//            helper.setTo(patient.getEmail());
            helper.setSubject(subject);
            helper.setText(text, true); // Enable HTML
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
        emailSender.send(message);
    }

    public void sendDietPlanCreationEmail(DietPlan dietPlan) {

        DietPlanDTO dietPlanDTO = mapper.mapToDietPlanDTO(dietPlan);

        String patientEmail = dietPlan.getUser().getEmail();
        String patientName = dietPlan.getUser().getFirst_name()/* + " " + dietPlan.getUser().getLast_name()*/;
        String clinic = dietPlan.getUser().getClinic();

        MimeMessage message = emailSender.createMimeMessage();
        String text = "<!DOCTYPE html>"
          + "<html lang=\"en\">"
          + "<head>"
          + "<meta charset=\"UTF-8\">"
          + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
          + "<title>3x6 Table</title>"
          + "<style>"
          + "table, th, td {"
          + "  border: 1px solid black;"
          + "  border-collapse: collapse;"
          + "}"
//          + "table { width: 100%; border-collapse: collapse; border: 1px solid #000; }"
//          + "th, td { border: 1px solid #000; padding: 8px; text-align: center; }"
//          + "th { background-color: #f2f2f2; }"
          + "</style>"
          + "</head>"
          + "<body>"
          + "<h1>Plan de dieta</h1>"
          + "<br>Hola <b>"
          + patientName
          + "</b>, se te ha asignado un nuevo plan de dieta:<br><br>"
          + "Meta: "
          + dietPlanDTO.getGoal()
          + "<br>Fecha de creación: "
          + dietPlanDTO.getDate()
          + "<br>Comentarios: "
          + dietPlanDTO.getComment()
          + "<br><br>"
          + "<table>"
          + "<thead>"
          + "<tr><th>Horario</th><th>Opción 1</th><th>Opción 2</th></tr>"
          + "</thead>"
          + "<tbody>"
          + "<tr><td>Desayuno</td><td>"
          + dietPlanDTO.getBreakfast1()
          + "</td><td>"
          + dietPlanDTO.getBreakfast2()
          + "</td></tr>"
          + "<tr><td>Colación 1</td><td>"
          + dietPlanDTO.getColation1_1()
          + "</td><td>"
          + dietPlanDTO.getColation1_2()
          + "</td></tr>"
          + "<tr><td>Comida</td><td>"
          + dietPlanDTO.getLunch1()
          + "</td><td>"
          + dietPlanDTO.getLunch2()
          + "</td></tr>"
          + "<tr><td>Colación 2</td><td>"
          +  dietPlanDTO.getColation2_1()
          + "</td><td>"
          + dietPlanDTO.getColation2_2()
          + "</td></tr>"
          + "<tr><td>Cena</td><td>"
          + dietPlanDTO.getDinner1()
          + "</td><td>"
          + dietPlanDTO.getDinner2()
          + "</td></tr>"
//          + "<tr><td>Row 6, Cell 1</td><td>Row 6, Cell 2</td><td>Row 6, Cell 3</td></tr>"
          + "</tbody>"
          + "</table>"
          + "</body>"
          + "</html>";

        String subject = "Nuevo Plan de Dieta para " + patientName + " - "+clinic;

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("no.reply.nutrisystem@gmail.com");
            helper.setTo("emendozag1602@alumno.ipn.mx");
//            helper.setTo(patientEmail);
            helper.setSubject(subject);
            helper.setText(text, true); // Enable HTML
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
        emailSender.send(message);
    }

}
