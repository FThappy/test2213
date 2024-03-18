package app.utils;

import app.DTO.response.StoreAccumulationUsedResDTO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ExcelUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_PATH = "D:\\Task/";

    private XSSFWorkbook workbook ;
    private XSSFSheet sheet;

    public void createSheet(String title) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(title);
    }

    public void createCell(Row row, int columnCount, Object value, CellStyle style, XSSFSheet sheet) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }
        if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        if (value instanceof String) {
            String strValue = (String) value;
            if (strValue.startsWith("=")) { // Kiểm tra xem giá trị có bắt đầu bằng '=' không
                cell.setCellFormula(strValue.substring(1)); // Thiết lập công thức
            } else {
                cell.setCellValue(strValue);
            }
        }
        if (value instanceof Timestamp) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            cell.setCellValue(dateFormat.format((Timestamp) value));
        }
        if (value == null) {
            cell.setCellValue((Integer) 0);
        }
        cell.setCellStyle(style);
    }

    public <T> void createHeaderRow(String title, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, title, style, sheet);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fields.length - 1));
        font.setFontHeightInPoints((short) 10);
        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        // Lặp qua từng trường và thêm vào header row
        int columnIndex = 0;
        for (Field field : fields) {
            String fieldName = field.getName();
            createCell(row, columnIndex, fieldName, style, sheet);
            columnIndex++;
        }
    }

    public <T> int writeData(List<T> listValue) {

        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);


        for (T value : listValue) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            Field[] fields = value.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(value);
                    createCell(row, columnCount++, fieldValue, style, sheet);
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle exception as needed
                }
            }
        }
        return rowCount;
    }

    public <T> void writeCustomerData(int rowCount, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        CellStyle styles = workbook.createCellStyle();
        XSSFFont fonts = workbook.createFont();
        Row row = sheet.getRow(1);
        fonts.setBold(true);
        fonts.setFontHeight(16);
        styles.setFont(fonts);
        createCell(row, fields.length + 2, "Thống kê", styles, sheet);
        createCell(row, fields.length + 3, "Số lượng store sử dụng AHM", styles, sheet);
        createCell(row, fields.length + 4, "Số lượng đơn sử dụng AHM", styles, sheet);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        Row row1 = sheet.getRow(2);
        createCell(row1, fields.length + 2, "Tổng số", style, sheet);
        createCell(row1, fields.length + 3, "=COUNT(A2:A" + rowCount + ")", style, sheet);
        createCell(row1, fields.length + 4, "=SUM(B2:B" + rowCount + ")", style, sheet);
    }

    public <T> int writeSingelData(T value) {

        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(value);
                createCell(row, columnCount++, fieldValue, style, sheet);
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // Handle exception as needed
            }
        }
        return rowCount;
    }

    public void exportCustomExcel(HttpServletResponse response,String title) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        exportLocalFile(title);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
    public <T> void exportDataToExcel(HttpServletResponse response, String title, Class<T> clazz, List<T> value) throws IOException {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(title);
        createHeaderRow(title, clazz);
        int rowCount = writeData(value);
        ServletOutputStream outputStream = response.getOutputStream();
        exportLocalFile(title);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void exportLocalFile(String title) {
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String filePath = DEFAULT_PATH + title + currentMonth + ".xlsx" ;
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean fileExist(String title,HttpServletResponse response) throws IOException {
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String filePath = DEFAULT_PATH + title + currentMonth + ".xlsx" ;
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream excel = new FileInputStream(file);
            workbook = new XSSFWorkbook(excel);
        } else {
            workbook = new XSSFWorkbook();
            return false;
        }
        exportToFile(filePath,response);

        return true;
    }
    private void exportToFile(String filePath,HttpServletResponse response) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



}
