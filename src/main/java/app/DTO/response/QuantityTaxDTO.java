package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityTaxDTO {
    private int storeId;
    private Number number;
}
