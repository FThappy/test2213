package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountOrderDTO {
    private int storeId;
    private Number quantityOrder;
}
