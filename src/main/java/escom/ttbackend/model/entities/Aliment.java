package escom.ttbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aliment")
public class Aliment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "id_aliment")
    private Long idAliment;
    @ManyToOne
    @JoinColumn(name = "aliment_group", referencedColumnName = "id_aliment_group", nullable = false)
    private AlimentGroup alimentGroup;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 10, nullable = false)
    private String quantity;
    @Column(length = 20, nullable = false)
    private String unit;
    @OneToMany(mappedBy = "aliment", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Meal> meals;

}
