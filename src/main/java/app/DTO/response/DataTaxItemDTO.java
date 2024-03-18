package app.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DataTaxItemDTO {
    private Integer storeId;
    private String name;
    private String address;
    private String phone;
    private Integer storePackageId;
    private Number number;

    public DataTaxItemDTO(StoreIdDTO storeIdDTO, QuantityTaxDTO quantityTaxDTO){
        this.storeId = storeIdDTO.getStoreId();
        this.name = storeIdDTO.getName();
        this.address = storeIdDTO.getAddress();
        this.phone = storeIdDTO.getPhone();
        this.storePackageId = storeIdDTO.getStorePackageId();
        this.number = quantityTaxDTO.getNumber();
    }
}
