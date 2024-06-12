package escom.ttbackend.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisisistDTO {
    private SmallDTO breakfast1;
    private SmallDTO colation1_1;
    private SmallDTO lunch1;
    private SmallDTO colation1_2;
    private SmallDTO dinner1;


    private SmallDTO breakfast2;
    private SmallDTO colation2_1;
    private SmallDTO lunch2;
    private SmallDTO colation2_2;
    private SmallDTO dinner2;


}
