package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantityInvoiceDTO {
    private Integer storeId;
    private Number quantityInvoice;
}
