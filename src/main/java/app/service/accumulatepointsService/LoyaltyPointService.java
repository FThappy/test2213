package app.service.accumulatepointsService;

import app.DTO.response.ExportStoreAccumulationResDTO;
import app.DTO.response.ExportStoreAccumulationUsedResDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface AccumulatePoints {

    public List<ExportStoreAccumulationResDTO> exportListStoreAccmulation(HttpServletResponse response) throws IOException;
    public List<ExportStoreAccumulationUsedResDTO> exportListStoreAccmulationUsed(HttpServletResponse response) throws IOException;

}
