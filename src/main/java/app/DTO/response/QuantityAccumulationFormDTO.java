package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountAccumulationFormDTO {
    private Integer storeId;
    private Integer quantityForm;
}
