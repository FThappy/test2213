package app.controller;

import app.service.accumulatepointsService.LoyaltyPointService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RequestMapping("/api/v1/report/accumulate-point")
public class LoyaltyPointController {

    @Autowired
    private LoyaltyPointService loyaltyPointService;
    @GetMapping("/export-to-excel")
    //test
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListStoreAccumulatePoints.xlsx";
        response.setHeader(headerKey, headerValue);
        loyaltyPointService.exportListStoreAccmulation(response);
    }
    @GetMapping("/export-point-used-to-excel")
    public void exportPointUsedToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListStoreAccumulatePointsUsed.xlsx";
        response.setHeader(headerKey, headerValue);
        loyaltyPointService.exportListStoreAccmulationUsed(response);
    }
}
