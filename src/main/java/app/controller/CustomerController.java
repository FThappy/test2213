package app.controller;

import app.service.customerService.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RequestMapping("/api/v1/report/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @GetMapping("/export-debt-to-excel")
    public void exportPointUsedToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListDebt.xlsx";
        response.setHeader(headerKey, headerValue);
        customerService.exportListDebt(response);
    }
}
