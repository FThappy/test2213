package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor

public class InvoiceDTO {
    private Integer storeId;
    private Number quantityUnrelease;
    private Number quantityRelease;
    private Number quantityCQT;
}
