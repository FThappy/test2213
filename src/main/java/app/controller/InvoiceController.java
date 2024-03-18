package app.controller;

import app.service.invoiceService.InvoiceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RequestMapping("/api/v1/report/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/export-me-invoice-to-excel")
    public void exportMeInvoiceToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListMeInvoice.xlsx";
        response.setHeader(headerKey, headerValue);
        invoiceService.getListMe_Invoice(response);
    }
    @GetMapping("/export-m-invoice-to-excel")
    public void exportMInvoiceToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListMInvoice.xlsx";
        response.setHeader(headerKey, headerValue);
        invoiceService.getListM_Invoice(response);
    }
    @GetMapping("/export-s-invoice-to-excel")
    public void exportSInvoiceToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListSInvoice.xlsx";
        response.setHeader(headerKey, headerValue);
        invoiceService.getListS_Invoice(response);
    }


}
