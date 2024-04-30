package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.model.enums.AppointmentStatus;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.AppointmentDTO;
import escom.ttbackend.presentation.dto.AppointmentRequest;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.repository.AppointmentRepository;
import escom.ttbackend.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NutriService{
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final Mapper mapper;
    public User save(User newUser){
        return userRepository.save(newUser);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void delete(String email) {
        userRepository.deleteById(email);
    }

    public boolean existsById(String email) {
        return userRepository.existsById(email);
    }

    public User getByEmail(String email) {
        return userRepository.findById(email).orElseThrow();
    }

    public List<UserDTO> getUsersByParentEmail(String parentEmail) {
        List<User> users = userRepository.findByParent_Email(parentEmail);
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(mapper.mapToUserDTO(user));
        }
        return userDTOS;
    }

    public AppointmentDTO scheduleAppointment(AppointmentRequest appointmentRequest, String email) {
        User nutritionist = userRepository.findByEmail(email).orElseThrow(); //TODO ERROR HANDLING
        User patient = userRepository.findByEmail(appointmentRequest.getPatient_email()).orElseThrow(); //TODO tmbn aki we
        if (validateParentEmailForUser(patient.getEmail(), nutritionist.getEmail())) {
            var appointment = Appointment.builder()
                    .nutritionist(nutritionist)
                    .starting_time(appointmentRequest.getStarting_time())
                    .patient(patient)
                    .ending_time(appointmentRequest.getEnding_time())
                    .appointmentStatus(AppointmentStatus.CONFIRMED)
                    .build();
            return mapper.mapToAppointmentDTO(appointmentRepository.save(appointment));
        }
        else
            throw new EntityExistsException("Patient is not part of nutritionist"); //TODO improve this exception handling
    }

    public List<AppointmentDTO> getAllApointments(String email) {
        List<Appointment> appointments = appointmentRepository.findByNutritionist_Email(email);
        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDTOS.add(mapper.mapToAppointmentDTO(appointment));
        }
        return appointmentDTOS  ;
    }
    public boolean validateParentEmailForUser(String userEmail, String parentEmail) {
        User user = userRepository.findById(userEmail).orElse(null);
        if (user != null) {
            return user.getParent().getEmail().equals(parentEmail);
        }
        return false;
    }

}
