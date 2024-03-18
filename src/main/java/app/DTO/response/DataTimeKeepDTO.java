package app.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DataTimeKeepDTO {
    private Integer storeId;
    private String name;
    private String address;
    private Integer merchantId;
    private Integer storePackageId;
    private Number numberSuccessTimeKeep;
    private Number numberNotDetermined;
    private Number numberOutRange;
    private Number numberCases;
    private Number numberMaxEmployee;
    private Number numberMinEmployee;
    public DataTimeKeepDTO(StoreTimeKeepDTO storeTimeKeepDTO, Number numberSuccessTimeKeep, Number numberNotDetermined,
                           Number numberOutRange, Number numberCases, Number numberMaxEmployee, Number numberMinEmployee){
        this.storeId = storeTimeKeepDTO.getStoreId();
        this.name = storeTimeKeepDTO.getName();
        this.address = storeTimeKeepDTO.getAddress();
        this.merchantId = storeTimeKeepDTO.getMerchantId();
        this.storePackageId = storeTimeKeepDTO.getStorePackageId();
        this.numberSuccessTimeKeep = numberSuccessTimeKeep;
        this.numberNotDetermined = numberNotDetermined;
        this.numberOutRange = numberOutRange;
        this.numberMaxEmployee =numberMaxEmployee;
        this.numberCases = numberCases;
        this.numberMinEmployee = numberMinEmployee;
    }
}
