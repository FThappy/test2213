package app.service.accumulatepointsService;

import app.DTO.response.StoreAccumulationResDTO;
import app.DTO.response.StoreAccumulationUsedResDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface LoyaltyPointService {

    public void exportListStoreAccmulation(HttpServletResponse response) throws IOException;
    public void exportListStoreAccmulationUsed(HttpServletResponse response) throws IOException;

}
