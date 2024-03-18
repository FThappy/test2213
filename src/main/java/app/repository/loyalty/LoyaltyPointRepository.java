package app.repository.loyalty;

import app.DTO.response.QuantityAccmulationUsedDTO;
import app.DTO.response.QuantityAccumulationFormDTO;
import app.DTO.response.StoreAccumulationUsedResDTO;

import java.util.List;

public interface LoyaltyPointRepository {
    public QuantityAccmulationUsedDTO queryListStoreAccmulationUsed(Object[] params);
    public QuantityAccumulationFormDTO quantityAccumulationFormDTO (Object[] params);

}
