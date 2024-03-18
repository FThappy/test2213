package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityActiveStoreDTO {
    private Integer storeId;
    private Number number;
}
