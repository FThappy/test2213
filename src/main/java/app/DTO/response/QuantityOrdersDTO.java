package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityOrdersDTO {
    private int storeId;
    private Integer number;
    private Double totalprice;

}
