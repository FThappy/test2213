package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class StoreIdDTO {
    private Integer storeId;
    private String name;
    private String address;
    private String phone;
    private Integer storePackageId;

    public StoreIdDTO(StoreIdDTO storeId){
        this.storeId = storeId.getStoreId();
        this.name = storeId.getName();
        this.address = storeId.getAddress();
        this.phone = storeId.getPhone();
        this.storePackageId = storeId.getStorePackageId();
    }


}
