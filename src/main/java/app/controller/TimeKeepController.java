package app.controller;

import app.DTO.response.InvoiceDTO;
import app.service.timekeepingService.TimeKeepingService;
import app.utils.CSVUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RequestMapping("/api/v1/report/timekeep")
public class TimeKeepController {

    @Autowired
    private TimeKeepingService timeKeepingService;
    @GetMapping("/export-timekeep-to-excel")
    public void exportTimeKeepToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=timekeep.csv");
        timeKeepingService.getListTimeKeeping(response);
    }
}
