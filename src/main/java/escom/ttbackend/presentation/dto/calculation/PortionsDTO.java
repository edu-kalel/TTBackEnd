package escom.ttbackend.presentation.dto.calculation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortionsDTO {
    private double verduras;
    private double frutas;
    private double cereales;
    private double cerealesConGrasa;
    private double leguminosas;
    private double alimentosDeOrigenAnimalMuyBajosEnGrasa;
    private double alimentosDeOrigenAnimalBajosEnGrasa;
    private double alimentosDeOrigenAnimalModeradosEnGrasa;
    private double alimentosDeOrigenAnimalAltoContenidoDeGrasa;
    private double lecheDescremada;
    private double LecheEntera;
    private double grasas;
    private double grasasConProteína;
    private double azucares;
    private double azucaresConGrasa;
    private double sumaKcal;
    private double sumaProteinas;
    private double sumaLipidos;
    private double sumaCarbohidratos;
}
