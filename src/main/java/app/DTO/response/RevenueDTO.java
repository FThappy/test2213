package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDTO {
    private Integer quantityOrders;
    private Double totalPrice;
}
