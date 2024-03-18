package app.utils;

import app.DTO.response.DataTimeKeepDTO;
import com.opencsv.CSVWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    public static final String DEFAULT_PATH = "D:\\Task/";

    private CSVWriter writer;

    public CSVWriter CSVUtilsToDowload(ServletOutputStream outputStream) {
        writer = new CSVWriter(new OutputStreamWriter(outputStream)) ;
        return writer;
    }
    public CSVWriter CSVUtilsToLocal(String title) throws IOException {
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        writer = new CSVWriter(new FileWriter(DEFAULT_PATH  + "/" + title + currentMonth + ".csv")) ;
        return writer;
    }


    public <T> void writeCSVHeader(Class<T> clazz,String title,CSVWriter writer) throws IOException {
        String[] header = {title};
        writer.writeNext(header);
        Field[] fields = clazz.getDeclaredFields();
        List<String> listField = new ArrayList<>();
        for (Field field : fields){
            String fieldName = field.getName();
            listField.add(fieldName);
        }
        writer.writeNext(listField.toArray(new String[listField.size()]));
        writer.flush();
    }

    public <T> void writeCSVData(List<T> values,CSVWriter writer){
        try {
            for(T value : values){
                Field[] fields = value.getClass().getDeclaredFields();
                List<Object> listFieldValue = new ArrayList<>();
                for(Field field : fields){
                    field.setAccessible(true);
                    Object fieldValue = setType(field.get(value));
                    listFieldValue.add(fieldValue);
                }
                writer.writeNext(listFieldValue.toArray(new String[listFieldValue.size()]));
            }
            writer.close();
            System.out.println("Complete");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public Object setType (Object fieldValue){
        if(fieldValue == null){
            return "0";
        }
        if(fieldValue instanceof String){
            return (String) fieldValue;
        }else {
            return fieldValue.toString();
        }
    }

    public boolean fileExist(String title, HttpServletResponse response) throws IOException {
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String filePath = DEFAULT_PATH  + "/" + title + currentMonth + ".csv" ;
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream csv = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = csv.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            csv.close();
            outputStream.close();
        } else {
            return false;
        }
        return true;
    }
//    private void exportLocalFile(String title) {
//        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
//        String filePath = DEFAULT_PATH + "/" + title + currentMonth + ".xlsx" ;
//        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//            workbook.write(fileOut);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
