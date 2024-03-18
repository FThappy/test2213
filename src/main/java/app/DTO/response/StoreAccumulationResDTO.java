package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreAccumulationResDTO {
    private Integer storeId;
    private String name;
    private String address;
    private String ward;
    private String district;
    private String province;
    private String phone;
    private Integer storePackageId;
    private Integer merchantId;
    private Integer quantityForm;

    public StoreAccumulationResDTO(StoreAccumulateDTO s , QuantityAccumulationFormDTO c){
        this.storeId = s.getStoreId();
        this.name = s.getName();
        this.address = s.getAddress();
        this.ward = s.getWard();
        this.district = s.getDistrict();
        this.province = s.getProvince();
        this.phone = s.getPhone();
        this.storePackageId = s.getStorePackageId();
        this.merchantId = s.getMerchantId();
        this.quantityForm = c.getQuantityForm();
    }
}
