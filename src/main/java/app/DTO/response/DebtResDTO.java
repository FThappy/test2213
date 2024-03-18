package app.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DebtResDTO {
    private Integer storeId;
    private String name;
    private String address;
    private String phone;
    private Integer storePackageId;
    private Number quantityCustomer;
    private Number quantityDebtCollection;
    private Number quantityWriteDebt;

    public DebtResDTO(StoreIdDTO storeId, Number quantityCustomer, Number quantityDebtCollection, Number quantityWriteDebt){
        this.storeId = storeId.getStoreId();
        this.name =  storeId.getName();
        this.address = storeId.getAddress();
        this.phone = storeId.getPhone();
        this.storePackageId = storeId.getStorePackageId();
        this.quantityCustomer = quantityCustomer;
        this.quantityDebtCollection = quantityDebtCollection;
        this.quantityWriteDebt = quantityWriteDebt;
    }

}
