package app.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ExportDataTimeKeepDTO {
    private Integer storeId;
    private String name;
    private String address;
    private Integer merchantId;
    private Integer storePackageId;
    private Number countSuccessTimeKeep;
    private Number countNotDetermined;
    private Number countOutRange;
    private Number countCases;
    private Number countMaxEmployee;
    private Number countMinEmployee;
    public ExportDataTimeKeepDTO(StoreTimeKeepDTO storeTimeKeepDTO){
        this.storeId = storeTimeKeepDTO.getStoreId();
        this.name = storeTimeKeepDTO.getName();
        this.address = storeTimeKeepDTO.getAddress();
        this.merchantId = storeTimeKeepDTO.getMerchantId();
        this.storePackageId = storeTimeKeepDTO.getStorePackageId();
    }
}
