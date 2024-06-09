package escom.ttbackend.presentation.dto.calculation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DietRequestBody {

    private String eqverduras;
    private String eqfrutas;
    private String eqcereales1;
    private String eqcereales2;
    private String eqleguminosas;
    private String eqaoa1;
    private String eqaoa2;
    private String eqaoa3;
    private String eqaoa4;
    private String eqleche1;
    private String eqleche2;
    private String eqleche3;
    private String eqleche4;
    private String eqaceites1;
    private String eqaceites2;
    private String eqazucar1;
    private String eqazucar2;
    private String totalKcal;
    private String porcentajeHco;
    private String porcentajeLip;
    private String porcentajePro;
    private String t_valid;
}
