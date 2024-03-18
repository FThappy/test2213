package app.service.StoreService;

import app.DTO.response.QuantityAHMGrabDTO;
import app.DTO.response.DataStoreConnectWebOrderResDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface StoreService {

    public  void exportStoreConnectWebOrder(HttpServletResponse response) throws IOException;

    public void exportCountAHM(HttpServletResponse response) throws IOException;

    public void exportCountGrab(HttpServletResponse response) throws IOException;

    public String exportPaymentMethodMPOS(HttpServletResponse response) throws IOException;
    public String exportPaymentMethodOCB(HttpServletResponse response) throws IOException;
    public String exportPaymentMethodTCB(HttpServletResponse response) throws IOException;

    public String exportActiveStore(Integer month,Integer year,HttpServletResponse response) throws IOException;
    public void exportNumberStore(String dateEnded,HttpServletResponse response) throws IOException;
    public void exportTop20Customer(String created ,String ended,HttpServletResponse response) throws IOException;

    public void exportRevenue(String created ,String ended,HttpServletResponse response) throws IOException;


}
