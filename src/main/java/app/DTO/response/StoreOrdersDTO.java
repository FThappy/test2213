package app.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class StoreOrdersDTO {
    private Integer storeId;
    private String name;
    private String address;
    private String phone;
}
