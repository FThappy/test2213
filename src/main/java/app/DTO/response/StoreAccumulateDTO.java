package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreAccumulateDTO {
    private Integer storeId;
    private String name;
    private String address;
    private String ward;
    private String district;
    private String province;
    private String phone;
    private Integer storePackageId;
    private Integer merchantId;

}
