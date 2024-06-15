package escom.ttbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aliment_group")
public class AlimentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "id_aliment_group")
    private Long idAlimentGroup;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(nullable = false, updatable = false)
    private int kcal;
    @Column(nullable = false, updatable = false)
    private int carbs;
    @Column(nullable = false, updatable = false)
    private int fats;
    @Column(nullable = false, updatable = false)
    private int proteins;
    @OneToMany(mappedBy = "alimentGroup", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Aliment> aliments;
}
