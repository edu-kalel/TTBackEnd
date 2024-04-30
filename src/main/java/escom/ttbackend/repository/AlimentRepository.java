package escom.ttbackend.repository;

import escom.ttbackend.model.entities.Aliment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlimentRepository extends JpaRepository<Aliment, String> {
}
