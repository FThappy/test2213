package app.service.StoreService;

import app.DTO.response.CountOrderDTO;
import app.DTO.response.ExportDataStoreConnectWebOrderDTO;
import app.DTO.response.ExportDataStoreConnectWebOrderResDTO;
import app.model.OrdersEntity;
import app.repository.OrdersRepository;
import app.repository.StoreRepository;
import app.utils.ExcelExportStoreConnectWebUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class StoreServiceImp implements StoreService {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    //test
    public List<OrdersEntity> exportDataToExcel() {
//        String sql = "SELECT s.server_id AS serverId, s.name AS storeName, sa.address AS storeAddress, " +
//                "sa.ward AS storeWard, sa.district AS storeDistrict, sa.province AS storeProvince, " +
//                "u.phone AS userPhone, s.store_package_id AS storePackageId, s.merchant_id AS merchantId " +
//                "FROM stores s, fnb_users.user_store_mappings usm, fnb_users.users u, store_addresses sa " +
//                "WHERE s.server_id = usm.store_id AND usm.owner = 1 AND u.client_id = usm.user_id " +
//                "AND s.server_id = sa.store_id " +
//                "ORDER BY s.server_id;";
//        String sql = "select * from stores";
//        Query query = entityManager.createNativeQuery(sql, ExportDataToExcelStoreDTO.class);
//        String sql= "select store_id as storeId, count(server_id) as quantityOrder from orders " +
//                "where store_id = 6 "
////                    + exportDataStoreConnectWebOrderDTO.getStoreId()
//                +
//                " and order_type in ('website', 'facebook', 'zalo') " +
//                " group by store_id " +
//                "order by store_id";
        String sql = "select * from fnb_orders.orders where store_id = 6";
        Query query = entityManager.createNativeQuery(sql, OrdersEntity.class);


        return query.getResultList();


//        List<ExportDataToExcelStoreDTO> result = query.setResultTransformer( Transformers.aliasToBean( ExportDataToExcelStoreDTO.class ) ).list();

//        return entity;
    }

    @Override
//    @Async("threadPoolTaskExecutor")
    public List<ExportDataStoreConnectWebOrderResDTO> exportStoreConnectWebOrder(HttpServletResponse response) throws IOException {
        try {
            String sql = "select distinct (s.server_id) as storeId\n" +
                    " , s.name as name, s.phone as phone,sa.address as address,sa.ward as ward,sa.district as district" +
                    ",sa.province as province,m.alias as alias,from_unixtime(ml.created_on) as createdOn, s.store_package_id as storePackageId \n" +
                    "from stores as s,store_addresses as sa, merchants as m, store_settings as ss,\n" +
                    "merchant_logs as ml\n" +
                    "where sa.store_id = s.server_id\n" +
                    "and s.merchant_id = m.server_id\n" +
                    "and ss.store_id = s.server_id\n" +
                    "and ss.setting_key = 'web_order'\n" +
                    "and ml.merchant_id = s.merchant_id\n" +
                    "and m.server_id != 11446\n" +
                    "and m.init_website = true\n" +
                    "group by s.server_id\n";
            Query query = entityManager.createNativeQuery(sql, ExportDataStoreConnectWebOrderDTO.class);
            List<ExportDataStoreConnectWebOrderDTO> listStoreConnectWebOrder = query.getResultList();
            ThreadPoolExecutor executorService = new ThreadPoolExecutor(30, 50, 180,
                    TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(180));
            List<ExportDataStoreConnectWebOrderResDTO> listExportDataStoreConnectWebOrder = new ArrayList<>();
            for (ExportDataStoreConnectWebOrderDTO storeConnectWebOrder : listStoreConnectWebOrder) {
                Callable<ExportDataStoreConnectWebOrderResDTO> callable = new CallableMergeData(storeConnectWebOrder);
                Future<ExportDataStoreConnectWebOrderResDTO> future = executorService.submit(callable);
                try {
                    ExportDataStoreConnectWebOrderResDTO result = future.get();
                    if (result != null) {
                        listExportDataStoreConnectWebOrder.add(result);
                    } else {
                        System.out.println("No result found for storeId: " + storeConnectWebOrder.getStoreId());
                    }
                } catch (InterruptedException e) {
                    // Handle interruption
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    future.cancel(true);
                }
//                Future future = executorService.submit(() -> {
//                    String sqlOrder = "select store_id,count(server_id) from fnb_orders.orders where store_id = 6 and order_type in ('website', 'facebook', 'zalo') group by store_id order by store_id ";
//                    Query queryOrder = entityManager.createNativeQuery(sqlOrder);
//                    Object[] result = (Object[]) queryOrder.getSingleResult();
//                    int storeId = (int) result[0];
//                    int countServerId = ((Number) result[1]).intValue();
//                    CountOrderDTO countOrderDTO = new CountOrderDTO(storeId, countServerId);
//
//                    ExportDataStoreConnectWebOrderResDTO exportDataStoreConnectWebOrderResDTO =
//                            new ExportDataStoreConnectWebOrderResDTO(storeConnectWebOrder, countOrderDTO);
//                    listExportDataStoreConnectWebOrder.add(exportDataStoreConnectWebOrderResDTO);
//
//                });
//                if(future.isDone()){
//                    System.out.println("Thanh cong");
//                }
            }
            executorService.shutdown();
            // Wait until all threads are finish
            while (!executorService.isTerminated()) {
                // Running ...
            }
            ExcelExportStoreConnectWebUtils exportUtils = new ExcelExportStoreConnectWebUtils(listExportDataStoreConnectWebOrder);
            exportUtils.exportDataToExcel(response);
            return listExportDataStoreConnectWebOrder;
        } catch (Exception e) {
            throw new RuntimeException();
        }


    }

    public class CallableMergeData implements Callable<ExportDataStoreConnectWebOrderResDTO> {

        private ExportDataStoreConnectWebOrderDTO exportDataStoreConnectWebOrderDTO;

        public CallableMergeData(ExportDataStoreConnectWebOrderDTO exportDataStoreConnectWebOrderDTO) {
            this.exportDataStoreConnectWebOrderDTO = exportDataStoreConnectWebOrderDTO;
        }

        @Override
        public ExportDataStoreConnectWebOrderResDTO call() throws Exception {
//            String sqlOrder = "select store_id as storeId, count(server_id) as quantityOrder from fnb_orders.orders " +
//                    " where store_id = 6 "
////                    + exportDataStoreConnectWebOrderDTO.getStoreId()
//                    +
//                    " and order_type in ('website', 'facebook', 'zalo') " +
//                    " group by store_id " +
//                    " order by store_id";
            String sql = "select store_id,count(server_id) from fnb_orders.orders where store_id = "+
                    exportDataStoreConnectWebOrderDTO.getStoreId()
                    + "  and order_type in ('website', 'facebook', 'zalo') group by store_id order by store_id ";
            Query query = entityManager.createNativeQuery(sql);
            Object[] result = (Object[]) query.getSingleResult();
            int storeId = (int) result[0];
            int countServerId = ((Number) result[1]).intValue();
            CountOrderDTO countOrderDTO = new CountOrderDTO(storeId, countServerId);
            System.out.println("Store ID: " + storeId + ", Count Server ID: " + countServerId);
//            Query queryOrder = entityManager.createNativeQuery(sqlOrder, CountOrderDTO.class);
//            System.out.println(queryOrder.getResultList());
//            CountOrderDTO countOrderDTO = (CountOrderDTO) queryOrder.getSingleResult();


            return new ExportDataStoreConnectWebOrderResDTO(exportDataStoreConnectWebOrderDTO, countOrderDTO);
        }
    }

}
