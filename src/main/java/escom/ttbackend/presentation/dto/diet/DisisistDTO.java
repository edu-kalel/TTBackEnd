package escom.ttbackend.presentation.dto.diet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisisistDTO{
    private List<SmallDTO> breakfast1;
    private List<SmallDTO> colation1_1;
    private List<SmallDTO> lunch1;
    private List<SmallDTO> colation1_2;
    private List<SmallDTO> dinner1;

    private List<SmallDTO> breakfast2;
    private List<SmallDTO> colation2_1;
    private List<SmallDTO> lunch2;
    private List<SmallDTO> colation2_2;
    private List<SmallDTO> dinner2;

}
