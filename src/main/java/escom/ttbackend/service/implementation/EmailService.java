package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class EmailService{
    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(
            String to, String subject, String text) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("sender@mail.com");
            helper.setTo("emendozag1602@alumno.ipn.mx");
//            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // Enable HTML
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
        emailSender.send(message);
    }

    public void sendsAppointementConfirmationMessage(User nutritionist, User patient, Appointment appointment) {
        MimeMessage message = emailSender.createMimeMessage();
        String text = "<h1>"
                + nutritionist.getClinic()
                + "</h1><br>Hola <b>"
                + patient.getFirst_name()
                + "</b>, la cita solicitada con tu Nutriólogo ("
                + nutritionist.getFirst_name()
                + ") ha sido aprobada para la siguiente fecha y hora: <br>"
                + appointment.getStartingTime()
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
                + "</b>, se te ha agendando con tu Nutriólogo ("
                + nutritionist.getFirst_name()
                + ") para la siguiente fecha y hora: <br>"
                + appointment.getStartingTime()
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

    public void sendMessageWithAttachment(
            String to, String subject, String text, String pathToAttachment){
        // ...

        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("no.reply.nutrisystem@gmail.com");
//        helper.setTo("stark02eliu@gmail.com");
            helper.setTo("emendozag1602@alumno.ipn.mx");
//            helper.setTo("jtirado@ipn.mx");
//        helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            FileSystemResource file
                    = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);
        }
        catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

        emailSender.send(message);
        // ...
    }
}
