package escom.ttbackend.service;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.presentation.dto.UserDTO;

import java.util.List;

public interface UserService {
    User save(User newUser);
    User update(User user);
    void delete(String email);
    boolean existsById(String email);
    User getByEmail(String email);
    List<UserDTO> getUsersByParentEmail(String parentEmail);
}
