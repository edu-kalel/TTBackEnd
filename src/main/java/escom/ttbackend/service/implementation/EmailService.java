package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService{
    @Autowired
    private JavaMailSender emailSender;


    public void sendsAppointementConfirmationMessage(User nutritionist, User patient, Appointment appointment) {
        MimeMessage message = emailSender.createMimeMessage();
        String text = "<h1>"
                + nutritionist.getClinic()
                + "</h1><br>Hola <b>"
                + patient.getFirst_name()
                + "</b>, la cita solicitada con tu Nutriólogo ("
                + nutritionist.getFirst_name()
                + ") ha sido aprobada para la siguiente fecha y hora: <br>"
                + "Fecha: " + appointment.getStartingTime().getDayOfMonth() + "/" + appointment.getStartingTime().getMonthValue() + "/" + appointment.getStartingTime().getYear()
                + "   Hora: " + appointment.getStartingTime().getHour() + ":" + appointment.getStartingTime().getMinute()
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
        String text = "<h1>"
                + nutritionist.getClinic()
                + "</h1><br>Hola <b>"
                + patient.getFirst_name()
                + "</b>, se te ha agendando una cita con tu Nutriólogo ("
                + nutritionist.getFirst_name()
                + ") para la siguiente fecha y hora: <br>"
                + "Fecha: " + appointment.getStartingTime().getDayOfMonth() + "/" + appointment.getStartingTime().getMonthValue() + "/" + appointment.getStartingTime().getYear()
                + "   Hora: " + appointment.getStartingTime().getHour() + ":" + appointment.getStartingTime().getMinute()
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
        String text = "<h1>"
          + nutritionist.getClinic()
          + "</h1><br>Hola <b>"
          + patient.getFirst_name()
          + "</b>, la cita con tu Nutriólogo ("
          + nutritionist.getFirst_name()
          + ") con la siguiente fecha y hora: <br>"
          + "Fecha: " + appointment.getStartingTime().getDayOfMonth() + "/" + appointment.getStartingTime().getMonthValue() + "/" + appointment.getStartingTime().getYear()
          + "   Hora: " + appointment.getStartingTime().getHour() + ":" + appointment.getStartingTime().getMinute()
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

}
