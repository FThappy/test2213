package app.DTO.response;

import lombok.*;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataStoreConnectWebOrderDTO {

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

}
