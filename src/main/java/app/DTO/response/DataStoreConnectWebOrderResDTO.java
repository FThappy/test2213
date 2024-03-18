package app.DTO.response;

import lombok.*;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataStoreConnectWebOrderResDTO {
    private int storeId;
    private String name;
    private String phone;
    private String address;
    private String ward;
    private String district;
    private String province;
    private String alias;
    private Timestamp createdOn;
    private int storePackageId;
    private Number quantityOrder;


    public DataStoreConnectWebOrderResDTO(DataStoreConnectWebOrderDTO e , QuantityOrderDTO c){
        this.storeId = e.getStoreId();
        this.name = e.getName();
        this.phone = e.getPhone();
        this.address = e.getAddress();
        this.ward = e.getWard();
        this.district = e.getDistrict();
        this.province = e.getProvince();
        this.alias = e.getAlias();
        this.createdOn = e.getCreatedOn();
        this.storePackageId = e.getStorePackageId();
        this.quantityOrder = c.getNumber();

    }
}
