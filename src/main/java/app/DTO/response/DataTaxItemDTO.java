package app.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ExportDataTaxItemDTO {
    private Integer storeId;
    private String name;
    private String address;
    private String phone;
    private Integer storePackageId;
    private Number number;

    public ExportDataTaxItemDTO(StoreIdDTO storeIdDTO, CountTaxDTO countTaxDTO){
        this.storeId = storeIdDTO.getStoreId();
        this.name = storeIdDTO.getName();
        this.address = storeIdDTO.getAddress();
        this.phone = storeIdDTO.getPhone();
        this.storePackageId = storeIdDTO.getStorePackageId();
        this.number = countTaxDTO.getNumber();
    }
}
