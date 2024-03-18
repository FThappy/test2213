package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityAccumulationFormDTO {
    private Integer storeId;
    private Integer quantityForm;
}
