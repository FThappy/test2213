package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreTimeKeepDTO {
    private Integer storeId;
    private String name;
    private String address;
    private Integer merchantId;
    private Integer storePackageId;
}
