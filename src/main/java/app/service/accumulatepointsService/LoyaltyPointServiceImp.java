package app.service.accumulatepointsService;

import app.DTO.response.*;
import app.repository.customer.CustomerRepository;
import app.repository.loyalty.LoyaltyPointRepository;
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

public class LoyaltyPointServiceImp implements LoyaltyPointService {

    public static final String LIST_STORE_ACCMULATION_USED = "ListStoreAccumulatePointsUsed";
    public static final String LIST_STORE_ACCMULATION = "ListStoreAccumulatePoints";

    @Autowired
    @Qualifier("mariadbOrdersJdbcTemplate")
    private JdbcTemplate mariadbOrdersJdbcTemplate;
    @Value("${default.number.Thread}")
    private int DEFAULT_NUMBER_THREAD;
    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private LoyaltyPointRepository loyaltyPointRepository;

    @Override
    public void exportListStoreAccmulationUsed(HttpServletResponse response) throws IOException {
        try {
            boolean fileExists = excelUtils.fileExist(LIST_STORE_ACCMULATION_USED, response);
            if (fileExists) {
                return ;
            }
            List<StoreAccumulateDTO> listStore = listStore();
            List<StoreAccumulationUsedResDTO> listStoreAccmulationUsed = new ArrayList();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (StoreAccumulateDTO store : listStore) {
                executorService.execute(() -> {
                    try {
                        Object[] params = {store.getStoreId()};
                        QuantityAccmulationUsedDTO countAccumulationUsedDTO = loyaltyPointRepository.queryListStoreAccmulationUsed(params);
                        if (countAccumulationUsedDTO != null) {
                            StoreAccumulationUsedResDTO storeAccumulationUsedResDTO = new StoreAccumulationUsedResDTO(store, countAccumulationUsedDTO);
                            listStoreAccmulationUsed.add(storeAccumulationUsedResDTO);
                        }
                    } catch (EmptyResultDataAccessException e) {
                        // Ghi nhận lỗi hoặc xử lý nó theo cách phù hợp với ứng dụng của bạn
                        System.out.println("Empty result for store id: " + store.getStoreId());
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
            excelUtils.exportDataToExcel(response, LIST_STORE_ACCMULATION_USED,StoreAccumulationUsedResDTO.class, listStoreAccmulationUsed);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void exportListStoreAccmulation(HttpServletResponse response) throws IOException {
        try {
            boolean fileExists = excelUtils.fileExist(LIST_STORE_ACCMULATION, response);
            if (fileExists) {
                return ;
            }
            List<StoreAccumulateDTO> listStore = listStore();
            List<StoreAccumulationResDTO> listStoreAccmulation = new ArrayList();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (StoreAccumulateDTO store : listStore) {
                executorService.execute(() -> {
                    try {
                        Object[] params = {store.getStoreId()};
                        QuantityAccumulationFormDTO quantityAccumulationFormDTO = loyaltyPointRepository.quantityAccumulationFormDTO(params);
                        if (quantityAccumulationFormDTO != null) {
                            StoreAccumulationResDTO storeAccumulationResDTO = new StoreAccumulationResDTO(store, quantityAccumulationFormDTO);
                            listStoreAccmulation.add(storeAccumulationResDTO);
                        }
                    } catch (EmptyResultDataAccessException e) {
                        // Ghi nhận lỗi hoặc xử lý nó theo cách phù hợp với ứng dụng của bạn
                        System.out.println("Empty result for store id: " + store.getStoreId());
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
            excelUtils.exportDataToExcel(response,LIST_STORE_ACCMULATION,StoreAccumulationResDTO.class, listStoreAccmulation);
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    public List<StoreAccumulateDTO> listStore() {
        List<Integer> listMerchantId = customerRepository.listMerchantId();
        List<StoreAccumulateDTO> listStoreAccumulates = new ArrayList();
        ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
        for (Integer merchantId : listMerchantId) {
            executorService.execute(() -> {
                Object[] params = {merchantId};
                List<StoreAccumulateDTO> storeAccumulates = storeRepository.queryStoreAccumulateDTOS(merchantId);
                if (storeAccumulates == null) {
                    Thread.currentThread().interrupt();
                } else {
                    listStoreAccumulates.addAll(storeAccumulates);
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return listStoreAccumulates;
    }




}
