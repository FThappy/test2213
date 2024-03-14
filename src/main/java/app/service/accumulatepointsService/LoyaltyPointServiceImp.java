package app.service.accumulatepointsService;

import app.DTO.response.*;
import app.utils.ExcelExportStoreAccumulatePoint;
import app.utils.ExcelExportStoreAccumulateUsed;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

public class AccumulatePointsImp implements AccumulatePoints {

    @Autowired
    @Qualifier("mariadbCustomersJdbcTemplate")
    private JdbcTemplate mariadbCustomersJdbcTemplate;
    @Autowired
    @Qualifier("mariadbStoresJdbcTemplate")
    private JdbcTemplate mariadbStoresJdbcTemplate;
    @Autowired
    @Qualifier("mariadbOrdersJdbcTemplate")
    private JdbcTemplate mariadbOrdersJdbcTemplate;

    @Override
    public List<ExportStoreAccumulationUsedResDTO> exportListStoreAccmulationUsed(HttpServletResponse response) throws IOException {
        try {
            List<StoreAccumulateDTO> listStore = listStore();
            List<ExportStoreAccumulationUsedResDTO> listStoreAccmulationUsed = new ArrayList();
            ExecutorService executorService = Executors.newFixedThreadPool(16);
            for (StoreAccumulateDTO store : listStore) {
                executorService.execute(() -> {
                    System.out.println(store.getStoreId());
                    String sql = "select store_id as storeId,count(*) as quantity from order_discounts where store_id = ?  " +
                            "  and discount_type = 'loyalty_card' group by store_id;";
                    try {
                        CountAccmulationUsedDTO countAccumulationUsedDTO = mariadbOrdersJdbcTemplate.queryForObject(sql, (rs, rownum) -> {
                            return new CountAccmulationUsedDTO(rs.getInt("storeid"),
                                    rs.getInt("quantity")
                            );
                        },store.getStoreId());
                        if (countAccumulationUsedDTO != null) {
                            ExportStoreAccumulationUsedResDTO exportStoreAccumulationUsedResDTO = new ExportStoreAccumulationUsedResDTO(store, countAccumulationUsedDTO);
                            listStoreAccmulationUsed.add(exportStoreAccumulationUsedResDTO);
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
            ExcelExportStoreAccumulateUsed excelExportStoreAccumulateUsed = new ExcelExportStoreAccumulateUsed(listStoreAccmulationUsed);
            excelExportStoreAccumulateUsed.exportDataToExcel(response);

            return listStoreAccmulationUsed;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<ExportStoreAccumulationResDTO> exportListStoreAccmulation(HttpServletResponse response) throws IOException {
        try {
            List<StoreAccumulateDTO> listStore = listStore();
            List<ExportStoreAccumulationResDTO> listStoreAccmulation = new ArrayList();
            ExecutorService executorService = Executors.newFixedThreadPool(16);
            for (StoreAccumulateDTO store : listStore) {
                executorService.execute(() -> {
                    System.out.println(store.getStoreId());
                    String sql = "select olc.store_id as storeId ,count(*) as quantity from order_loyalty_cards olc where olc.store_id =  " +
                            "    group by olc.store_id;";
                    try {
                        CountAccumulationFormDTO countAccumulationFormDTO = mariadbOrdersJdbcTemplate.queryForObject(sql, (rs, rownum) -> {
                            return new CountAccumulationFormDTO(rs.getInt("storeid"),
                                    rs.getInt("quantity")
                            );
                        },store.getStoreId());
                        if (countAccumulationFormDTO != null) {
                            ExportStoreAccumulationResDTO exportStoreAccumulationResDTO = new ExportStoreAccumulationResDTO(store, countAccumulationFormDTO);
                            listStoreAccmulation.add(exportStoreAccumulationResDTO);
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
            ExcelExportStoreAccumulatePoint excelExportStoreAccumulatePoint = new ExcelExportStoreAccumulatePoint(listStoreAccmulation);
            excelExportStoreAccumulatePoint.exportDataToExcel(response);

            return listStoreAccmulation;
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    public List<StoreAccumulateDTO> listStore() {
        List<Integer> listMerchantId = listMerchantId();
//        String listId = listMerchantId.stream()
//                .map(Object::toString).collect(Collectors.joining(","));
//        String sql = "SELECT s.server_id AS storeId, s.name as name, "+
//                " sa.address AS address, sa.ward AS ward, sa.district AS district, sa.province AS province,"+
//                " s.phone AS phone, s.store_package_id AS storePackageId, s.merchant_id AS merchantId "+
//                "FROM stores AS s " +
//                "LEFT JOIN store_addresses AS sa ON s.server_id = sa.store_id " +
//                "WHERE s.merchant_id IN (" + listId + ")";
//        List<StoreAccumulateDTO> listStoreAccumulates = mariadbStoresJdbcTemplate.query(sql,(rs, rownum)-> {
//        return new StoreAccumulateDTO(  rs.getInt("storeid"),
//                rs.getString("name"),
//                rs.getString("address"),
//                rs.getString("ward"),
//                rs.getString("district"),
//                rs.getString("province"),
//                rs.getString("phone"),
//                rs.getInt("storepackageid"),
//                rs.getInt("merchantid")
//        );});
        List<StoreAccumulateDTO> listStoreAccumulates = new ArrayList();
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        for (Integer merchantId : listMerchantId) {
            executorService.execute(() -> {
                String sql = "SELECT s.server_id AS storeId, s.name as name, " +
                        " sa.address AS address, sa.ward AS ward, sa.district AS district, sa.province AS province," +
                        " s.phone AS phone, s.store_package_id AS storePackageId, s.merchant_id AS merchantId " +
                        "FROM stores AS s " +
                        "LEFT JOIN store_addresses AS sa ON s.server_id = sa.store_id " +
                        "WHERE s.merchant_id = " + merchantId;
                List<StoreAccumulateDTO> storeAccumulates = mariadbStoresJdbcTemplate.query(sql, (rs, rownum) -> {
                    return new StoreAccumulateDTO(rs.getInt("storeid"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("ward"),
                            rs.getString("district"),
                            rs.getString("province"),
                            rs.getString("phone"),
                            rs.getInt("storepackageid"),
                            rs.getInt("merchantid")
                    );
                });
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

    public List<Integer> listMerchantId() {
//        String sql = "select distinct (merchantId) from LoyaltyCardsEntity ";
//        Query query = entityManager.createQuery(sql,Integer.class);

//        return query.getResultList();
        String sql = "select distinct merchant_id from fnb_customers.loyalty_cards";
        List<Integer> ids = mariadbCustomersJdbcTemplate.queryForList(sql, Integer.class);

        return ids;

    }
}
