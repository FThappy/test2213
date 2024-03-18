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
public class RevenueExport {

    private Integer quantityOrders;
    private String totalPrice;
    public RevenueExport (RevenueDTO revenueDTO){
        this.quantityOrders = revenueDTO.getQuantityOrders();
        DecimalFormat vndFormat = new DecimalFormat("#,###.## VND");
        this.totalPrice = vndFormat.format(revenueDTO.getTotalPrice());
    }
}
