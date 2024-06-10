package escom.ttbackend.presentation.dto.calculation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietResponseBody {

    private double verduras_com;
    private double frutas_com;
    private double cereales_com;
    private double cerealesGrasa_com;
    private double leguminosas_com;
    private double aoa1_com;
    private double aoa2_com;
    private double aoa3_com;
    private double aoa4_com;
    private double leche1_com;
    private double leche2_com;
    private double leche3_com;
    private double leche4_com;
    private double grasas_com;
    private double grasas2_com;
    private double azucares_com;
    private double azucares2_com;
    @JsonProperty("1")
    private double _1;
    @JsonProperty("2")
    private double _2;
    @JsonProperty("3")
    private double _3;
    @JsonProperty("4")
    private double _4;
    private double valpPro;
    private double valpLip;
    private double valpHco;
    private double rgKcal;
    private String rgPro;
    private String rgLip;
    private String rgHco;
    private String porKcal;
    private String porPro;
    private String porLip;
    private String porHco;
    private String porcentajeHcosimples;

    // Getters and Setters
}
