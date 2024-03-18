package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityPaymentDTO {
    private Integer storeId;
    private Number number;
}
