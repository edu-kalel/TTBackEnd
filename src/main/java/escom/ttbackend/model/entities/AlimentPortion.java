package escom.ttbackend.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aliment_portion")
public class AlimentPortion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_aliment_portion;
    @Column(nullable = false)
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "id_meal", referencedColumnName = "id_meal", nullable = false)
    private Meal meal;
    @OneToOne
    @JoinColumn(name="aliment_name", referencedColumnName = "name", nullable = false)
    private Aliment aliment;
}
