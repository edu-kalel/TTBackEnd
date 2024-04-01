package escom.ttbackend.service.implementation;

import escom.ttbackend.model.entities.User;
import escom.ttbackend.repository.UserRepository;
import escom.ttbackend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User save(User newUser){
        return userRepository.save(newUser);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(String email) {
        userRepository.deleteById(email);
    }

    @Override
    public boolean existsById(String email) {
        return userRepository.existsById(email);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findById(email).orElseThrow();
    }

}
