package app.controller;

import app.service.productService.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RequestMapping("/api/v1/report/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/export-list-tax-item")
    public void exportTaxItem(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListTaxItem.xlsx";
        response.setHeader(headerKey, headerValue);
        productService.exportListTaxItem(response);
    }
    @GetMapping("/export-list-special-time")
    public void exportSpecialTime(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListSpecialTime.xlsx";
        response.setHeader(headerKey, headerValue);
        productService.exportListSpecialTime(response);
    }
    @GetMapping("/export-list-default-item")
    public void exportDefaultItem(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListDefaultItem.xlsx";
        response.setHeader(headerKey, headerValue);
        productService.exportListDefaultItem(response);
    }
}
