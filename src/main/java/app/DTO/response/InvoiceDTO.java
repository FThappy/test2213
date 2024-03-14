package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor

public class MeInvoiceDTO {
    private Integer storeId;
    private Number quantityUnrelease;
    private Number quantityRelease;
    private Number quantityCQT;

    public MeInvoiceDTO(Integer storeId){
        this.storeId = storeId;
    }
}
