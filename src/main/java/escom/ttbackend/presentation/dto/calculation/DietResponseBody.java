package escom.ttbackend.presentation.dto.calculation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietResponseBody {

    @JsonProperty("verduras_com")
    private double verduras;
    @JsonProperty("frutas_com")
    private double frutas;
    @JsonProperty("cereales_com")
    private double cereales;
    @JsonProperty("cerealesGrasa_com")
    private double cerealesConGrasa;
    @JsonProperty("leguminosas_com")
    private double leguminosas;
    @JsonProperty("aoa1_com")
    private double alimentosDeOrigenAnimalMuyBajosEnGrasa;
    @JsonProperty("aoa2_com")
    private double alimentosDeOrigenAnimalBajosEnGrasa;
    @JsonProperty("aoa3_com")
    private double alimentosDeOrigenAnimalModeradosEnGrasa;
    @JsonProperty("aoa4_com")
    private double alimentosDeOrigenAnimalAltoContenidoDeGrasa;
    @JsonProperty("leche1_com")
    private double lecheDescremada;
    @JsonProperty("leche2_com")
    private double lecheSemiDescremada;
    @JsonProperty("leche3_com")
    private double LecheEntera;
    @JsonProperty("leche4_com")
    private double lecheCA;
    @JsonProperty("grasas_com")
    private double grasas;
    @JsonProperty("grasas2_com")
    private double grasasConProteina;
    @JsonProperty("azucares_com")
    private double azucares;
    @JsonProperty("azucares2_com")
    private double azucaresConGrasa;
    @JsonProperty("1")
    private double sumaKcal;
    @JsonProperty("2")
    private double sumaProteinas;
    @JsonProperty("3")
    private double sumaLipidos;
    @JsonProperty("4")
    private double sumaCarbohidratos;
    @JsonProperty("valpPro")
    private double kcalProporcionadasPorProteinas;
    @JsonProperty("valpLip")
    private double kcalProporcionadasPorLipidos;
    @JsonProperty("valpHco")
    private double kcalProporcionadasPorCarbohidratos;
    @JsonProperty("rgKcal")
    private double rgKcal;
    @JsonProperty("rgPro")
    private String rgPro;
    @JsonProperty("rgLip")
    private String rgLip;
    @JsonProperty("rgHco")
    private String rgHco;
    @JsonProperty("porKcal")
    private String porKcal;
    @JsonProperty("porPro")
    private String porPro;
    @JsonProperty("porLip")
    private String porLip;
    @JsonProperty("porHco")
    private String porHco;
    @JsonProperty("porcentajeHcosimples")
    private String porcentajeHcosimples;

    // Getters and Setters
}
