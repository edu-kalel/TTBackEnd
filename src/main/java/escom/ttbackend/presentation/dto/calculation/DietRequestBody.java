package escom.ttbackend.presentation.dto.calculation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietRequestBody {
    private String totalKcal;
    private String porcentajeHco;
    private String porcentajeLip;
    private String porcentajePro;
}
