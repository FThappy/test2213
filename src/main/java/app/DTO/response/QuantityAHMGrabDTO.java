package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityAHMGrabDTO {
    private Integer storeId;
    private Number number;
    private String type;
}
