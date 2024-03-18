package app.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DataDefaultItemDTO {
    private Integer storeId;
    private String name;
    private String address;
    private String phone;
    private Integer storePackageId;
    private Number number;

    public DataDefaultItemDTO(StoreIdDTO storeIdDTO, QuantiyDefaultItem quantiyDefaultItem){
        this.storeId = storeIdDTO.getStoreId();
        this.name = storeIdDTO.getName();
        this.address = storeIdDTO.getAddress();
        this.phone = storeIdDTO.getPhone();
        this.storePackageId = storeIdDTO.getStorePackageId();
        this.number = quantiyDefaultItem.getNumber();
    }
}
