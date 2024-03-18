package app.service.productService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ProductService {
    public String exportListTaxItem(HttpServletResponse response) throws IOException;

    public String exportListSpecialTime(HttpServletResponse response) throws IOException;

    public String exportListDefaultItem(HttpServletResponse response) throws IOException;
}
