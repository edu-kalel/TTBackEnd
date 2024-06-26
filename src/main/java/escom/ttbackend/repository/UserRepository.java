package escom.ttbackend.repository;

import escom.ttbackend.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
//    Optional<User> findByEmail(String email);

    List<User> findByParent_Email(String parentEmail);

    boolean existsByClinic(String clinic);

    List<User> findByClinic(String clinic);
}
