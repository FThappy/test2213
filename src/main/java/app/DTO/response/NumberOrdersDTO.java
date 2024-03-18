package app.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;

@Data
@Getter
@Setter
@AllArgsConstructor
public class NumberOrdersDTO {
    private Integer storeId;
    private String name;
    private String address;
    private String phone;
    private Integer quantityOrder;
    private String totalPrice;

    public NumberOrdersDTO(StoreOrdersDTO storeOrdersDTO, QuantityOrdersDTO quantityOrdersDTO){
        this.storeId = storeOrdersDTO.getStoreId();
        this.name = storeOrdersDTO.getName();
        this.address = storeOrdersDTO.getAddress();
        this.phone = storeOrdersDTO.getPhone();
        this.quantityOrder = quantityOrdersDTO.getNumber();
        DecimalFormat vndFormat = new DecimalFormat("#,###.## VND");
        this.totalPrice = vndFormat.format(quantityOrdersDTO.getTotalprice());
    }
}
