package app.service.invoiceService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface InvoiceService {

    public String getListMe_Invoice(HttpServletResponse response) throws IOException;
    public String getListM_Invoice(HttpServletResponse response) throws IOException;
    public String getListS_Invoice(HttpServletResponse response) throws IOException;

}
