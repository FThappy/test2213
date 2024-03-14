package app.service.StoreService;

import app.DTO.response.ExportDataStoreConnectWebOrderResDTO;
import app.model.OrdersEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface StoreService {
    public List<OrdersEntity> exportDataToExcel();

    public  List<ExportDataStoreConnectWebOrderResDTO> exportStoreConnectWebOrder(HttpServletResponse response) throws IOException;
}
