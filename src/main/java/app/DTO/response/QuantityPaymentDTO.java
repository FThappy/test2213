package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountPaymentDTO {
    private Integer storeId;
    private Number number;
}
