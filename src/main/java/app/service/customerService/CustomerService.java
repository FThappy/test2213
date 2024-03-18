package app.service.customerService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface CustomerService {
    public String exportListDebt(HttpServletResponse response) throws IOException;
}
