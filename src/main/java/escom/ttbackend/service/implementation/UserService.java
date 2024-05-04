package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.Appointment;
import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.Mapper;
import escom.ttbackend.presentation.dto.SimpleUserDTO;
import escom.ttbackend.presentation.dto.UserDTO;
import escom.ttbackend.repository.AppointmentRepository;
import escom.ttbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final Mapper mapper;
    public User save(User newUser){
        return userRepository.save(newUser);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public UserDTO getUserDTObyID(String email){
        return mapper.mapToUserDTO(userRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist")));
    }

    @Transactional
    public void delete(String email) {
        List<Appointment> appointments = appointmentRepository.findByPatient_Email(email);
        appointmentRepository.deleteAll(appointments);
        userRepository.deleteById(email);
    }

    public boolean existsById(String email) {
        return userRepository.existsById(email);
    }

    public User getByEmail(String email) {
        return userRepository.findById(email).orElseThrow();
    }
    public boolean validateParentEmailForUser(String userEmail, String parentEmail) {
        User user = userRepository.findById(userEmail).orElse(null);//TODO IMPROOOOVE FFS
        if (user != null) {
            return user.getParent().getEmail().equals(parentEmail);
        }
        return false;
    }
    public boolean isFromSameClinic(String clinic, String userEmail) {
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
}
