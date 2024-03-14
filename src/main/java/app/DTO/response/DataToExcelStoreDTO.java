package app.DTO.response;


//s.server_id
//     , s.name, sa.address, sa.ward, sa.district, sa.province, u.phone, s.store_package_id, FROM_UNIXTIME(s.created_on), s.merchant_id

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportDataToExcelStoreDTO {
     private int serverId;
     private String name;
     private String address;
     private String ward;
     private String district;
     private String province;
     private String phone;
     private int storePackageId;
     private int merchantId;

}
