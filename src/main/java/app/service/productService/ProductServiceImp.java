package app.service.productService;

import app.DTO.response.*;
import app.repository.product.ProductRepository;
import app.repository.store.StoreRepository;
import app.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImp implements ProductService {

    public static final String LIST_TAX_ITEM = "ListTaxItem";
    public static final String LIST_SPECIAL_TIME = "ListSpecialTime";
    public static final String LIST_DEFAULT_ITEM = "ListDefaultItem";


    @Autowired
    @Qualifier("mariadbProductsJdbcTemplate")
    private JdbcTemplate mariadbProductsJdbcTemplate;
    @Autowired
    @Qualifier("mariadbOrdersJdbcTemplate")
    private JdbcTemplate mariadbOrdersJdbcTemplate;
    @Value("${default.number.Thread}")
    private int DEFAULT_NUMBER_THREAD;
    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public String exportListTaxItem(HttpServletResponse response) throws IOException {
        boolean fileExists = excelUtils.fileExist(LIST_TAX_ITEM,response);
        if(fileExists) {
            return "thành công" ;
        }
        try {
            List<StoreIdDTO> listTaxStoreId = storeRepository.queryListStoreTaxItem();
            List<DataTaxItemDTO> listTaxItem = new ArrayList();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (StoreIdDTO storeId : listTaxStoreId) {
                executorService.execute(() -> {
                    try {
                        Object[] params = {storeId.getStoreId()};
                        QuantityTaxDTO quantityTaxDTO = productRepository.queryQuantityTaxDTO(params);
                        if (quantityTaxDTO != null) {
                            DataTaxItemDTO dataTaxItemDTO = new DataTaxItemDTO(storeId, quantityTaxDTO);
                            listTaxItem.add(dataTaxItemDTO);
                        }
                    } catch (EmptyResultDataAccessException e) {
                        // Ghi nhận lỗi hoặc xử lý nó theo cách phù hợp với ứng dụng của bạn
                        System.out.println("Empty result for store id: " + storeId);
                    } catch (Exception e) {
                        // Xử lý các ngoại lệ khác nếu có
                        e.printStackTrace();
                    }
                });
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            excelUtils.exportDataToExcel(response, LIST_TAX_ITEM, DataTaxItemDTO.class, listTaxItem);
            return "thành công";
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    @Override
    public String exportListSpecialTime(HttpServletResponse response) throws IOException {

        boolean fileExists = excelUtils.fileExist(LIST_SPECIAL_TIME,response);
        if(fileExists) {
            return "thành công" ;
        }
        try {

            List<StoreIdDTO> listStoreId = storeRepository.queryListStoreSpecialTime();
            List<DataSpectialTimeDTO> listSpecialTime = new ArrayList();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (StoreIdDTO storeId : listStoreId) {
                executorService.execute(() -> {
                    try {
                        Object[] params = {storeId.getStoreId()};
                        QuantitySpecialTime quantitySpecialTime = productRepository.queryQuantitySpecialTime(params);
                        if (quantitySpecialTime != null) {
                            DataSpectialTimeDTO dataSpectialTimeDTO = new DataSpectialTimeDTO(storeId, quantitySpecialTime);
                            listSpecialTime.add(dataSpectialTimeDTO);
                        }
                    } catch (EmptyResultDataAccessException e) {
                        // Ghi nhận lỗi hoặc xử lý nó theo cách phù hợp với ứng dụng của bạn
                        System.out.println("Empty result for store id: " + storeId);
                    } catch (Exception e) {
                        // Xử lý các ngoại lệ khác nếu có
                        e.printStackTrace();
                    }
                });
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            excelUtils.exportDataToExcel(response, LIST_SPECIAL_TIME, DataSpectialTimeDTO.class, listSpecialTime);
            return "thành công";
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String exportListDefaultItem(HttpServletResponse response) throws IOException {

        boolean fileExists = excelUtils.fileExist(LIST_DEFAULT_ITEM,response);
        if(fileExists) {
            return "thành công" ;
        }
        try {
            List<StoreIdDTO> listStoreId = storeRepository.queryListStoreDefaultItem();
            List<DataDefaultItemDTO> listDefaultItem = new ArrayList();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (StoreIdDTO storeId : listStoreId) {
                executorService.execute(() -> {
                    try {
                        Object[] params = {storeId.getStoreId()};
                        QuantiyDefaultItem quantiyDefaultItem = productRepository.queryQuantityDefaultItem(params);
                        if (quantiyDefaultItem != null) {
                            DataDefaultItemDTO dataDefaultItemDTO = new DataDefaultItemDTO(storeId, quantiyDefaultItem);
                            listDefaultItem.add(dataDefaultItemDTO);
                        }
                    } catch (EmptyResultDataAccessException e) {
                        // Ghi nhận lỗi hoặc xử lý nó theo cách phù hợp với ứng dụng của bạn
                        System.out.println("Empty result for store id: " + storeId);
                    } catch (Exception e) {
                        // Xử lý các ngoại lệ khác nếu có
                        e.printStackTrace();
                    }
                });
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            excelUtils.exportDataToExcel(response, LIST_DEFAULT_ITEM, DataDefaultItemDTO.class, listDefaultItem);
            return "thành công";
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
