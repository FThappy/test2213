package app.controller;

import app.service.StoreService.StoreService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@RequestMapping("/api/v1/report/store")
public class StoresController {
    @Autowired
    private StoreService storeService;
    @GetMapping("/export-list-store-connect-web-to-excel")
    public void exportListStoreConnectWebToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=StoreConnectToWebOrder.xlsx";
        response.setHeader(headerKey, headerValue);
        storeService.exportStoreConnectWebOrder(response);
    }
    @GetMapping("/export-list-AHM")
    public void exportToExcelAHM(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListShipTypeAHAMOVE.xlsx";
        response.setHeader(headerKey, headerValue);
        storeService.exportCountAHM(response);
    }

    @GetMapping("/export-list-Grab")
    public void exportToExcelGrab(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListShipTypeGrab.xlsx";
        response.setHeader(headerKey, headerValue);
        storeService.exportCountGrab(response);
    }
    @GetMapping("/export-list-MPOS")
    public void exportPayMentMPOS(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListPayMentMPOS.xlsx";
        response.setHeader(headerKey, headerValue);
        storeService.exportPaymentMethodMPOS(response);
    }
    @GetMapping("/export-list-OCB")
    public void exportPayMentOCB(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListPayMentOCB.xlsx";
        response.setHeader(headerKey, headerValue);
        storeService.exportPaymentMethodOCB(response);
    }
    @GetMapping("/export-list-TCB")
    public void exportPayMentTCB(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListPayMentTCB.xlsx";
        response.setHeader(headerKey, headerValue);
        storeService.exportPaymentMethodTCB(response);
    }

    @GetMapping("/export-active-store-to-excel")
    public void exportActiveStoreToExcel(@RequestParam(value = "month") String monthReq, @RequestParam(value = "year") String yearReq, HttpServletResponse response) throws IOException {
        int month = Integer.parseInt(monthReq);
        int year = Integer.parseInt(yearReq);

        if (monthReq == null || yearReq == null || monthReq.isEmpty() || yearReq.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số month hoặc year.");
            return;
        }
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListStoreActiveStore.xlsx";
        response.setHeader(headerKey, headerValue);
        storeService.exportActiveStore(month,year,response);
    }
    @GetMapping("/export-number-store-to-excel")
    public void exportNumberStoreToExcel(@RequestParam(value = "dateEnded") String dateEndedReq, HttpServletResponse response) throws IOException {
        if (dateEndedReq == null || dateEndedReq.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ngày.");
            return;
        }
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListNumberStore.xlsx";
        response.setHeader(headerKey, headerValue);
        storeService.exportNumberStore(dateEndedReq,response);
    }
    @GetMapping("/export-top20-store-to-excel")
    public void exportTop20StoreToExcel(@RequestParam(value = "dateCreated") String dateCreatedReq,@RequestParam(value = "dateEnded") String dateEndedReq, HttpServletResponse response) throws IOException {
        if (dateCreatedReq == null || dateEndedReq == null || dateCreatedReq.isEmpty() || dateEndedReq.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số month hoặc year.");
            return;
        }
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListTop20Store.xlsx";
        response.setHeader(headerKey, headerValue);
        storeService.exportTop20Customer(dateCreatedReq,dateEndedReq,response);
    }
    @GetMapping("/export-revenue-to-excel")
    public void exportRevenueToExcel(@RequestParam(value = "dateCreated") String dateCreatedReq,@RequestParam(value = "dateEnded") String dateEndedReq, HttpServletResponse response) throws IOException {
        if (dateCreatedReq == null || dateEndedReq == null || dateCreatedReq.isEmpty() || dateEndedReq.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số month hoặc year.");
            return;
        }
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ListRevenueStore.xlsx";
        response.setHeader(headerKey, headerValue);
        storeService.exportRevenue(dateCreatedReq,dateEndedReq,response);
    }



}
