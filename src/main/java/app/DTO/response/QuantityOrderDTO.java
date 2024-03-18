package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityOrderDTO {
    private int storeid;
    private Number number;
}
