package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountInvoiceDTO {
    private Integer storeId;
    private Number quantityInvoice;
}
