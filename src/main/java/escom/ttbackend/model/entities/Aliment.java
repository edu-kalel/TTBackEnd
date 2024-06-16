package escom.ttbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import escom.ttbackend.model.converter.FractionConverter;
import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.Fraction;

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
    @Column(length = 100, nullable = false)
    private String name;
    @Column(length = 100, nullable = false)
    @Convert(converter = FractionConverter.class)
    private Fraction quantity;
    @Column(length = 100, nullable = false)
    private String unit;
    @OneToMany(mappedBy = "aliment", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Meal> meals;

}
