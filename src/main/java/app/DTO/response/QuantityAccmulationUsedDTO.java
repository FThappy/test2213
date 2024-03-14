package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountAccmulationUsedDTO {
    private Integer storeId;
    private Integer quantityForm;
}
