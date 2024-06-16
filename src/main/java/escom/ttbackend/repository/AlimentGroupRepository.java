package escom.ttbackend.repository;

import escom.ttbackend.model.entities.AlimentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlimentGroupRepository extends JpaRepository<AlimentGroup, Long> {

  AlimentGroup findByName(String name);

}
