package app.service.timekeepingService;

import app.DTO.response.QuantityTimeKeepDTO;
import app.DTO.response.DataTimeKeepDTO;
import app.DTO.response.StoreTimeKeepDTO;
import app.repository.store.StoreRepository;
import app.repository.timekeeping.TimeKeepRepository;
import app.utils.CSVUtils;
import com.opencsv.CSVWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class TimeKeepingServiceImp implements TimeKeepingService {

    public static final String LIST_TIME_KEEP = "Danh sách chấm công";
    @Autowired
    @Qualifier("mariadbOrdersJdbcTemplate")
    private JdbcTemplate mariadbOrdersJdbcTemplate;
    @Autowired
    @Qualifier("mariadbTimekeepingJdbcTemplate")
    private JdbcTemplate mariadbTimekeepingJdbcTemplate;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private TimeKeepRepository timeKeepRepository;

    @Override
    public String getListTimeKeeping(HttpServletResponse response) throws IOException {
        try {
            CSVUtils csvUtils = new CSVUtils();
            boolean fileExists = csvUtils.fileExist(LIST_TIME_KEEP, response);
            if (fileExists) {
                System.out.println("Thành công");
                return "Thành công";
            }

            List<StoreTimeKeepDTO> listStore = new ArrayList<StoreTimeKeepDTO>();
            List<QuantityTimeKeepDTO> listSuccessTimeKeep = new ArrayList<QuantityTimeKeepDTO>();
            List<QuantityTimeKeepDTO> listNotDetermined = new ArrayList<QuantityTimeKeepDTO>();
            List<QuantityTimeKeepDTO> listOutRange = new ArrayList<QuantityTimeKeepDTO>();
            List<QuantityTimeKeepDTO> listCases = new ArrayList<QuantityTimeKeepDTO>();
            List<QuantityTimeKeepDTO> listMaxEmployee = new ArrayList<QuantityTimeKeepDTO>();
            List<QuantityTimeKeepDTO> listMinEmployee = new ArrayList<QuantityTimeKeepDTO>();

            CompletableFuture<Void> getListStore = CompletableFuture.runAsync(() -> {
                try {
                    List<StoreTimeKeepDTO> listStoreQuery = storeRepository.queryListStoreTimeKeep();
                    listStore.addAll(listStoreQuery);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });

            CompletableFuture<Void> getListSuccessTimeKeep = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityTimeKeepDTO> quantitySuccessTimeKeep = timeKeepRepository.queryListSuccessTimeKeep();
                    listSuccessTimeKeep.addAll(quantitySuccessTimeKeep);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });

            CompletableFuture<Void> getListNotDetermined = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityTimeKeepDTO> quantityNotDetermined = timeKeepRepository.queryListNotDetermined();
                    listNotDetermined.addAll(quantityNotDetermined);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListOutRange = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityTimeKeepDTO> quantityOutRange = timeKeepRepository.queryListOutRange();
                    listOutRange.addAll(quantityOutRange);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListCases = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityTimeKeepDTO> quantityCases = timeKeepRepository.queryListCases();
                    listCases.addAll(quantityCases);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListMaxEmployee = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityTimeKeepDTO> quantityMaxEmployee = timeKeepRepository.queryListMaxEmployees();
                    listMaxEmployee.addAll(quantityMaxEmployee);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListMinEmployee = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityTimeKeepDTO> quantityMinEmployee = timeKeepRepository.queryListMinEmployees();
                    listMinEmployee.addAll(quantityMinEmployee);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture.allOf(getListStore, getListSuccessTimeKeep, getListNotDetermined, getListOutRange, getListCases, getListMaxEmployee, getListMinEmployee).join();
            List<DataTimeKeepDTO> exportDataTimeKeep = mergeData(listStore, listSuccessTimeKeep, listNotDetermined, listOutRange, listCases, listMaxEmployee, listMinEmployee);
            // Export to download
            ServletOutputStream outputStream = response.getOutputStream();
            CSVWriter csvWriter = csvUtils.CSVUtilsToDowload(outputStream);
            csvUtils.writeCSVHeader(DataTimeKeepDTO.class, LIST_TIME_KEEP, csvWriter);
            csvUtils.writeCSVData(exportDataTimeKeep, csvWriter);
            // Export to local file
            CSVWriter csvWriterToLocal = csvUtils.CSVUtilsToLocal(LIST_TIME_KEEP);
            csvUtils.writeCSVHeader(DataTimeKeepDTO.class, LIST_TIME_KEEP, csvWriterToLocal);
            csvUtils.writeCSVData(exportDataTimeKeep, csvWriterToLocal);
            outputStream.close();
            return "thành công";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    private List<DataTimeKeepDTO> mergeData(List<StoreTimeKeepDTO> listStore,
                                            List<QuantityTimeKeepDTO> listSuccessTimeKeep,
                                            List<QuantityTimeKeepDTO> listNotDetermined,
                                            List<QuantityTimeKeepDTO> listOutRange,
                                            List<QuantityTimeKeepDTO> listCases,
                                            List<QuantityTimeKeepDTO> listMaxEmployee,
                                            List<QuantityTimeKeepDTO> listMinEmployee) {
        Map<Integer, Number> successTimeKeepMap = listSuccessTimeKeep.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getNumber()
        ));
        Map<Integer, Number> notDeterminedMap = listNotDetermined.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getNumber()
        ));
        Map<Integer, Number> outRangeMap = listOutRange.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getNumber()
        ));
        Map<Integer, Number> casesMap = listCases.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getNumber()
        ));
        Map<Integer, Number> maxEmployeeMap = listMaxEmployee.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getNumber()
        ));
        Map<Integer, Number> minEmployeeMap = listMinEmployee.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getNumber()
        ));
        List<DataTimeKeepDTO> dataTimeKeepDTOs = new ArrayList<>();

        listStore.stream().forEach(store -> {
            DataTimeKeepDTO dataTimeKeepDTO = new DataTimeKeepDTO(store, successTimeKeepMap.get(store.getStoreId()),
                    notDeterminedMap.get(store.getStoreId()),
                    outRangeMap.get(store.getStoreId()),
                    casesMap.get(store.getStoreId()),
                    maxEmployeeMap.get(store.getStoreId()),
                    minEmployeeMap.get(store.getStoreId())
            );
            dataTimeKeepDTOs.add(dataTimeKeepDTO);
        });
        return dataTimeKeepDTOs;
    }
}

