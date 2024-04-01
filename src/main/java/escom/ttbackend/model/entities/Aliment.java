package escom.ttbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import escom.ttbackend.model.enums.Unit;
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
@Table(name = "aliment")
public class Aliment implements Serializable {
    @Id
    @Column(nullable = false, length = 50)
    private String name;
    @Enumerated(EnumType.STRING)
    private Unit unit;
    @OneToOne(mappedBy = "aliment")
    @JsonBackReference
    private AlimentPortion alimentPortion;
}
