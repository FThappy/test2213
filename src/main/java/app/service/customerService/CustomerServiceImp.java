package app.service.customerService;

import app.DTO.response.QuantityDebtDTO;
import app.DTO.response.DebtResDTO;
import app.DTO.response.StoreIdDTO;
import app.repository.customer.CustomerRepository;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImp implements CustomerService {

    public static final String LIST_DEBT = "ListDebt";
    @Autowired
    @Qualifier("mariadbProductsJdbcTemplate")
    private JdbcTemplate mariadbProductsJdbcTemplate;
    @Autowired
    @Qualifier("mariadbOrdersJdbcTemplate")
    private JdbcTemplate mariadbOrdersJdbcTemplate;
    @Autowired
    @Qualifier("mariadbCustomersJdbcTemplate")
    private JdbcTemplate mariadbCustomersJdbcTemplate;
    @Value("${default.number.Thread}")
    private int DEFAULT_NUMBER_THREAD;
    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public String exportListDebt(HttpServletResponse response) throws IOException {
        try {
            boolean fileExists = excelUtils.fileExist(LIST_DEBT, response);
            if (fileExists) {
                return "thành công";
            }
            List<StoreIdDTO> listStore = storeRepository.queryListStoreDebt();
            List<QuantityDebtDTO> countCustomer = new ArrayList<QuantityDebtDTO>();
            CompletableFuture<Void> getListCustomer = CompletableFuture.runAsync(() -> {
                System.out.println("Start");
                ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
                for (StoreIdDTO storeId : listStore) {
                    executorService.execute(() -> {
                        try {
                            Object[] params = {storeId.getStoreId()};
                            QuantityDebtDTO quantityCustomer = customerRepository.queryListCustomer(params);
                            if (quantityCustomer != null) {
                                countCustomer.add(quantityCustomer);
                            }
                        } catch (EmptyResultDataAccessException e) {
                            // Ghi nhận lỗi hoặc xử lý nó theo cách phù hợp với ứng dụng của bạn
                            System.out.println("Empty result for store id: " + storeId.getStoreId());
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
                System.out.println("Completed");
            });

            List<QuantityDebtDTO> countDebtCollection = new ArrayList<QuantityDebtDTO>();
            CompletableFuture<Void> getListDebtCollection = CompletableFuture.runAsync(() -> {
                ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
                for (StoreIdDTO storeId : listStore) {
                    executorService.execute(() -> {
                        try {
                            Object[] params = {storeId.getStoreId()};
                            QuantityDebtDTO quantityDebtCollection = customerRepository.queryListDebtCollection(params);
                            if (quantityDebtCollection != null) {
                                countDebtCollection.add(quantityDebtCollection);
                            }
                        } catch (EmptyResultDataAccessException e) {
                            // Ghi nhận lỗi hoặc xử lý nó theo cách phù hợp với ứng dụng của bạn
                            System.out.println("Empty result for store id: " + storeId.getStoreId());
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
                System.out.println("Completed");
            });
            List<QuantityDebtDTO> countWriteDebt = new ArrayList<QuantityDebtDTO>();
            CompletableFuture<Void> getListWriteDebt = CompletableFuture.runAsync(() -> {
                System.out.println("Start");
                ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
                for (StoreIdDTO storeId : listStore) {
                    executorService.execute(() -> {
                        try {
                            Object[] params = {storeId.getStoreId()};
                            QuantityDebtDTO quantityWriteDebt = customerRepository.queryListWriteDebt(params);
                            if (quantityWriteDebt != null) {
                                countWriteDebt.add(quantityWriteDebt);
                            }
                        } catch (EmptyResultDataAccessException e) {
                            // Ghi nhận lỗi hoặc xử lý nó theo cách phù hợp với ứng dụng của bạn
                            System.out.println("Empty result for store id: " + storeId.getStoreId());
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
                System.out.println("Completed");
            });
            CompletableFuture.allOf(getListCustomer, getListWriteDebt, getListDebtCollection).join();
            List<DebtResDTO> debtExport = mergeData(listStore, countCustomer, countWriteDebt, countDebtCollection);
            excelUtils.exportDataToExcel(response, LIST_DEBT, DebtResDTO.class, debtExport);
            return "thành công";
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private List<DebtResDTO> mergeData(List<StoreIdDTO> listStoreId, List<QuantityDebtDTO> listCustomer, List<QuantityDebtDTO> listWriteDebt, List<QuantityDebtDTO> listDebtCollection) {

        Map<Integer, Number> quantityCustomer = listCustomer.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getNumber()
        ));
        Map<Integer, Number> quantityWriteDebt = listWriteDebt.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getNumber()
        ));

        Map<Integer, Number> quantityDebtCollection = listDebtCollection.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getNumber()
        ));
        List<DebtResDTO> debtResDTOlist = new ArrayList<>();
        listStoreId.stream().forEach(storeId -> {
            DebtResDTO debtResDTO = new DebtResDTO(storeId, quantityCustomer.get(storeId.getStoreId()),
                    quantityWriteDebt.get(storeId.getStoreId()),
                    quantityDebtCollection.get(storeId.getStoreId())
            );
            debtResDTOlist.add(debtResDTO);
        });
        return debtResDTOlist;
    }
}
