package app.repository.product;

import app.DTO.response.QuantitySpecialTime;
import app.DTO.response.QuantityTaxDTO;
import app.DTO.response.QuantiyDefaultItem;

public interface ProductRepository {
    public QuantityTaxDTO queryQuantityTaxDTO(Object[] parameter);
    public QuantitySpecialTime queryQuantitySpecialTime(Object[] parameter);
    public QuantiyDefaultItem queryQuantityDefaultItem(Object[] parameter);


}
