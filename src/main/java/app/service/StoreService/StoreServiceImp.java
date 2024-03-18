package app.service.StoreService;

import app.DTO.response.*;
import app.repository.orders.OrdersRepository;
import app.repository.store.StoreRepository;
import app.utils.*;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

@Service
public class StoreServiceImp implements StoreService {

    public static final String STORE_USE_AHM = "Danh sách store dùng AHM";
    public static final String STORE_USE_GRAB = "Danh sách store dùng Grab";
    public static final String STORE_USE_MPOS = "Danh sách sử dụng MPOS";
    public static final String STORE_USE_OCB = "Danh sách sử dụng OCB";
    public static final String STORE_USE_TCB = "Danh sách sử dụng TCB";
    public static final String ACTIVE_STORE = "Số lượng store còn hoạt động có số đơn hàng thuộc các khoảng";
    public static final String NUMBER_STORE = "Số lượng store";
    public static final String TOP_20_CUSTOMER = "Top20Customer";
    public static final String REVENUE = "Chỉ số doanh thu";
    public static final String STORE_CONNECT_WEB_ORDER = "Danh sách kết nối";
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ThreadUtils threadUtils;
    @Autowired
    @Qualifier("mariadbOrdersJdbcTemplate")
    private JdbcTemplate mariadbOrdersJdbcTemplate;
    @Autowired
    @Qualifier("mariadbOrders2JdbcTemplate")
    private JdbcTemplate mariadbOrders2JdbcTemplate;
    @Autowired
    @Qualifier("mariadbStoresJdbcTemplate")
    private JdbcTemplate mariadbStoresJdbcTemplate;
    @Value("${default.number.Thread}")
    private int DEFAULT_NUMBER_THREAD;
    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Override
//    @Async("threadPoolTaskExecutor")
    public void exportStoreConnectWebOrder(HttpServletResponse response) throws IOException {
        boolean fileExists = excelUtils.fileExist(STORE_CONNECT_WEB_ORDER,response);
        if(fileExists) {
            return ;
        }
        try {
            List<DataStoreConnectWebOrderDTO> listStoreConnectWebOrder = storeRepository.listStoreConnectWeborder();
            System.out.println(listStoreConnectWebOrder);
            List<DataStoreConnectWebOrderResDTO> listExportDataStoreConnectWebOrder = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (DataStoreConnectWebOrderDTO storeConnectWebOrder : listStoreConnectWebOrder) {
                executorService.execute(() -> {
                    String sqls = """
                                select store_id as storeid, count(server_id) as number
                                from fnb_orders.orders
                                where store_id = ?
                                and order_type in ('website', 'facebook', 'zalo')
                                group by store_id
                                order by store_id
                            """;
                    Object[] params = {storeConnectWebOrder.getStoreId()};
                    QuantityOrderDTO quantityOrderDTO = ordersRepository.queryOrderUtilsForObject(sqls, QuantityOrderDTO.class, params);
                    if (quantityOrderDTO != null) {
                        DataStoreConnectWebOrderResDTO exportDataStoreConnectWebOrderDTO = new DataStoreConnectWebOrderResDTO(storeConnectWebOrder, quantityOrderDTO);
                        listExportDataStoreConnectWebOrder.add(exportDataStoreConnectWebOrderDTO);
                    }
                });
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            excelUtils.exportDataToExcel(response, STORE_CONNECT_WEB_ORDER, DataStoreConnectWebOrderResDTO.class, listExportDataStoreConnectWebOrder);
        } catch (Exception e) {
            throw new RuntimeException();
        }


    }

    @Override
    public void exportCountAHM(HttpServletResponse response) throws IOException {
        boolean fileExists = excelUtils.fileExist(STORE_USE_AHM,response);
        if(fileExists) {
            return ;
        }
        List<QuantityAHMGrabDTO> listCountAHM = new ArrayList();
        CompletableFuture<Void> countAHM = CompletableFuture.runAsync(() -> {
            try {
                List<Integer> listStoreId = storeRepository.queryListStoreAHM();
                ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
                for (Integer storeId : listStoreId) {
                    executorService.execute(() -> {
                        String sqlCount = "select count(o.server_id) as number from orders as o join shipments s on o.store_id = s.store_id and o.client_id = s.order_id  " +
                                " where o.store_id = ? " +
                                "  and s.shipping_method_type = 'ahamove';";
                        try {
                            Object[] params = {storeId};
                            Number number = ordersRepository.queryOrderUtilsForSingleData(sqlCount, Number.class, params);
                            if (number != null) {
                                QuantityAHMGrabDTO quantityAHMGrabDTO = new QuantityAHMGrabDTO(storeId, number, "AHAMOVE");
                                listCountAHM.add(quantityAHMGrabDTO);
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
                threadUtils.waitForFinish(executorService);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        CompletableFuture.allOf(countAHM).join();
        excelUtils.createSheet(STORE_USE_AHM);
        excelUtils.createHeaderRow(STORE_USE_AHM, QuantityAHMGrabDTO.class);
        int row = excelUtils.writeData(listCountAHM);
        excelUtils.writeCustomerData(row, QuantityAHMGrabDTO.class);
        excelUtils.exportCustomExcel(response, STORE_USE_AHM);
    }

    @Override
    public void exportCountGrab(HttpServletResponse response) throws IOException {
        boolean fileExists = excelUtils.fileExist(STORE_USE_GRAB,response);
        if(fileExists) {
            return ;
        }
        List<QuantityAHMGrabDTO> listCountGrab = new ArrayList();
        try {
            List<Integer> listStoreId = storeRepository.queryListStoreGrab();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (Integer storeId : listStoreId) {
                executorService.execute(() -> {
                    String sqlCount = """
                                select count(o.server_id) as number
                                from orders as o
                                join shipments s on o.store_id = s.store_id and o.client_id = s.order_id
                                where o.store_id = ?
                                and s.shipping_method_type = 'grabexpress'
                            """;
                    try {
                        Object[] params = {storeId};
                        Number number = ordersRepository.queryOrderUtilsForSingleData(sqlCount, Number.class, params);
                    } catch (EmptyResultDataAccessException e) {
                        // Ghi nhận lỗi hoặc xử lý nó theo cách phù hợp với ứng dụng của bạn
                        System.out.println("Empty result for store id: " + storeId);
                    } catch (Exception e) {
                        // Xử lý các ngoại lệ khác nếu có
                        e.printStackTrace();
                    }
                });
            }
            threadUtils.waitForFinish(executorService);

        } catch (Exception e) {
            throw new RuntimeException();
        }
        excelUtils.createSheet(STORE_USE_GRAB);
        excelUtils.createHeaderRow(STORE_USE_GRAB, QuantityAHMGrabDTO.class);
        int row = excelUtils.writeData(listCountGrab);
        excelUtils.writeCustomerData(row, QuantityAHMGrabDTO.class);
        excelUtils.exportCustomExcel(response, STORE_USE_GRAB);
    }

    @Override
    public String exportPaymentMethodMPOS(HttpServletResponse response) throws IOException {
        boolean fileExists = excelUtils.fileExist(STORE_USE_MPOS,response);
        if(fileExists) {
            return "thành công" ;
        }
        try {
            List<Integer> listStoreId = storeRepository.queryListStoreMPOS();
            List<QuantityPaymentDTO> listQuantityPaymentDTO = new ArrayList();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (Integer storeId : listStoreId) {
                executorService.execute(() -> {
                    String sql = """
                                select o.store_id as storeId, count(o.server_id) as number
                                from orders as o, order_payments as op
                                where o.store_id = ?
                                and o.client_id = op.order_id
                                and o.store_id = op.store_id
                                and op.payment_method_type = 'mPOS'
                                group by o.store_id
                                order by o.store_id
                            """;
                    try {
                        Object[] params = {storeId};
                        QuantityPaymentDTO quantityPaymentDTO = ordersRepository.queryOrderUtilsForObject(sql, QuantityPaymentDTO.class, params);
                        if (quantityPaymentDTO != null) {
                            listQuantityPaymentDTO.add(quantityPaymentDTO);
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
            threadUtils.waitForFinish(executorService);
            excelUtils.createSheet(STORE_USE_MPOS);
            excelUtils.createHeaderRow(STORE_USE_MPOS, QuantityPaymentDTO.class);
            int row = excelUtils.writeData(listQuantityPaymentDTO);
            excelUtils.writeCustomerData(row, QuantityPaymentDTO.class);
            excelUtils.exportCustomExcel(response, STORE_USE_MPOS);
            return "thành công";
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    @Override
    public String exportPaymentMethodOCB(HttpServletResponse response) throws IOException {
        try {
            boolean fileExists = excelUtils.fileExist(STORE_USE_OCB,response);
            if(fileExists) {
                return "thành công" ;
            }
            List<Integer> listStoreId = storeRepository.queryListStoreOCB();
            List<QuantityPaymentDTO> listQuantityPaymentDTO = new ArrayList();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (Integer storeId : listStoreId) {
                executorService.execute(() -> {
                    System.out.println(storeId);
                    String sql = """
                                select o.store_id as storeId, count(o.server_id) as number
                                from orders as o, order_payments as op
                                where o.store_id = ?
                                and o.client_id = op.order_id
                                and o.store_id = op.store_id
                                and op.payment_method_type = 'ocb'
                                group by o.store_id
                                order by o.store_id
                            """;
                    try {
                        Object[] params = {storeId};
                        QuantityPaymentDTO quantityPaymentDTO = ordersRepository.queryOrderUtilsForObject(sql, QuantityPaymentDTO.class, params);
                        if (quantityPaymentDTO != null) {
                            listQuantityPaymentDTO.add(quantityPaymentDTO);
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
            threadUtils.waitForFinish(executorService);
            excelUtils.createSheet(STORE_USE_OCB);
            excelUtils.createHeaderRow(STORE_USE_OCB, QuantityPaymentDTO.class);
            int row = excelUtils.writeData(listQuantityPaymentDTO);
            excelUtils.writeCustomerData(row, QuantityPaymentDTO.class);
            excelUtils.exportCustomExcel(response, STORE_USE_OCB);
            return "thành công";
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    @Override
    public String exportPaymentMethodTCB(HttpServletResponse response) throws IOException {
        boolean fileExists = excelUtils.fileExist(STORE_USE_TCB,response);
        if(fileExists) {
            return "thành công" ;
        }
        try {
            List<Integer> listStoreId = storeRepository.queryListStoreTCB();
            List<QuantityPaymentDTO> listQuantityPaymentDTO = new ArrayList();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (Integer storeId : listStoreId) {
                executorService.execute(() -> {
                    System.out.println(storeId);
                    String sql = """
                                select o.store_id as storeId, count(o.server_id) as number
                                from orders as o, order_payments as op
                                where o.store_id = ?
                                and o.client_id = op.order_id
                                and o.store_id = op.store_id
                                and op.payment_method_type = 'tcb'
                                group by o.store_id
                                order by o.store_id
                            """;
                    try {
                        Object[] params = {storeId};
                        QuantityPaymentDTO quantityPaymentDTO = ordersRepository.queryOrderUtilsForObject(sql, QuantityPaymentDTO.class, params);
                        if (quantityPaymentDTO != null) {
                            listQuantityPaymentDTO.add(quantityPaymentDTO);
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
            threadUtils.waitForFinish(executorService);
            excelUtils.createSheet(STORE_USE_TCB);
            excelUtils.createHeaderRow(STORE_USE_TCB, QuantityPaymentDTO.class);
            int row = excelUtils.writeData(listQuantityPaymentDTO);
            excelUtils.writeCustomerData(row, QuantityPaymentDTO.class);
            excelUtils.exportCustomExcel(response, STORE_USE_TCB);
            return "thành công";
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String exportActiveStore(Integer month, Integer year, HttpServletResponse response) throws IOException {

        try {
            String period = "("+month+"-"+year+")";
            boolean fileExists = excelUtils.fileExist(ACTIVE_STORE+period,response);
            if(fileExists) {
                return "thành công" ;
            }
            DateTimeDTO dateTimeDTO = getDate(month, year);
            List<Integer> listStoreId = storeRepository.queryListActiveStore();
            System.out.println(listStoreId);
            List<QuantityActiveStoreDTO> listCountActiveStore = new ArrayList<QuantityActiveStoreDTO>();
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
            for (Integer storeId : listStoreId) {
                executorService.execute(() -> {
                    String sql = """
                            SELECT o.store_id as storeId,count(o.server_id) as number FROM fnb_orders.orders as o
                            WHERE o.created_on between ? and ?
                            AND o.store_id = ?
                            group by o.store_id;
                            """;
                    try {
                        Object[] params = { dateTimeDTO.created, dateTimeDTO.ended, storeId};
                        QuantityActiveStoreDTO quantityActiveStoreDTO = ordersRepository.queryOrderUtilsForObject(sql,
                                QuantityActiveStoreDTO.class, params);

                        if (quantityActiveStoreDTO != null) {
                            listCountActiveStore.add(quantityActiveStoreDTO);
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
            threadUtils.waitForFinish(executorService);
            DistanceActiveStore distanceActiveStore = new DistanceActiveStore();
            listCountActiveStore.stream().forEach(countActiveStore -> {
                if (countActiveStore.getNumber().intValue() < 7) {
                    distanceActiveStore.setLess7(distanceActiveStore.getLess7() + 1);
                }
                if (countActiveStore.getNumber().intValue() < 10 && countActiveStore.getNumber().intValue() >= 7) {
                    distanceActiveStore.setLess10(distanceActiveStore.getLess10() + 1);
                }
                if (countActiveStore.getNumber().intValue() < 23 && countActiveStore.getNumber().intValue() >= 10) {
                    distanceActiveStore.setLess23(distanceActiveStore.getLess23() + 1);
                }
                if (countActiveStore.getNumber().intValue() < 30 && countActiveStore.getNumber().intValue() >= 23) {
                    distanceActiveStore.setLess30(distanceActiveStore.getLess30() + 1);
                }
                if (countActiveStore.getNumber().intValue() > 30) {
                    distanceActiveStore.setMore30(distanceActiveStore.getMore30() + 1);
                }
            });
            System.out.println(distanceActiveStore);
            excelUtils.createSheet(ACTIVE_STORE+period);
            excelUtils.createHeaderRow(ACTIVE_STORE+period, DistanceActiveStore.class);
            int row = excelUtils.writeSingelData(distanceActiveStore);
            excelUtils.exportCustomExcel(response, ACTIVE_STORE+period);
            return "thành công";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public DateTimeDTO getDate(Integer month, Integer year) {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        Timestamp createdTime = Timestamp.valueOf(firstDayOfMonth.atStartOfDay());
        Timestamp endedTime = Timestamp.valueOf(lastDayOfMonth.atTime(23, 59, 59));
        int createdAt = (int) (createdTime.getTime() / 1000L); // chia cho 1000 để chuyển từ milliseconds sang seconds
        int endedAt = (int) (endedTime.getTime() / 1000L); // chia cho 1000 để chuyển từ milliseconds sang seconds
        return new DateTimeDTO(createdAt, endedAt);

    }

    @Override
    public void exportNumberStore(String dateEnded, HttpServletResponse response) throws IOException {
        DateTimeDTO dateTimeDTO = getDateTime(dateEnded);
        String period = "("+dateEnded+")";
        boolean fileExists = excelUtils.fileExist(NUMBER_STORE+period,response);
        if(fileExists) {
            return ;
        }
        List<Integer> listStoreTrial = new ArrayList<Integer>();
        List<Integer> listStoreExpired = new ArrayList<Integer>();
        List<Integer> listStoreValid = new ArrayList<Integer>();

        CompletableFuture<Void> getListTrialStore = CompletableFuture.runAsync(() -> {
            try {
                List<Integer> listStoreId = storeRepository.queryListTrialStore();
                listStoreTrial.addAll(listStoreId);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        CompletableFuture<Void> getListValidStore = CompletableFuture.runAsync(() -> {
            try {
                Object[] params = {dateTimeDTO.ended};
                List<Integer> listStoreId = storeRepository.queryListValidStore(params);
                listStoreValid.addAll(listStoreId);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        CompletableFuture<Void> getListExpiredStore = CompletableFuture.runAsync(() -> {
            try {
                Object[] params = {dateTimeDTO.ended};
                List<Integer> listStoreId = storeRepository.queryListExpiredStore(params);
                listStoreExpired.addAll(listStoreId);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        CompletableFuture.allOf(getListTrialStore, getListValidStore, getListExpiredStore).join();
        NumberStoreDTO numberStore = new NumberStoreDTO(listStoreTrial.size(), listStoreExpired.size(), listStoreValid.size());
        excelUtils.createSheet(NUMBER_STORE+period);
        excelUtils.createHeaderRow(NUMBER_STORE+period, NumberStoreDTO.class);
        int row = excelUtils.writeSingelData(numberStore);
        excelUtils.exportCustomExcel(response, NUMBER_STORE+period);
    }

    public DateTimeDTO getDateTime(String dateEnded) {
        Timestamp ended = Timestamp.valueOf(dateEnded + " 23:59:59");
        int endedAt = (int) (ended.getTime() / 1000L); // chia cho 1000 để chuyển từ milliseconds sang seconds
        return new DateTimeDTO(0, endedAt);
    }


    @Override
    public void exportTop20Customer(String created, String ended, HttpServletResponse response) throws IOException {
        DateTimeDTO dateTime = getDateTime(created, ended);
        String period = "("+created+"-"+ended+")";
        boolean fileExists = excelUtils.fileExist(TOP_20_CUSTOMER+period,response);
        if(fileExists) {
            return ;
        }
        List<StoreOrdersDTO> listStoreId = storeRepository.listStoreIdCustomer();
        PriorityQueue<NumberOrdersDTO> listOrders = new PriorityQueue<>(Comparator.comparingInt(NumberOrdersDTO::getQuantityOrder).reversed());
        ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_NUMBER_THREAD);
        for (StoreOrdersDTO store : listStoreId) {
            executorService.execute(() -> {
                String sql = """
                        SELECT o.store_id as storeId,count(o.server_id) as number , sum(total_item_price) as totalprice FROM fnb_orders.orders as o
                        WHERE o.created_on between ? and ?
                        AND o.store_id = ?
                        group by o.store_id;
                        """;
                try {
                    Object[] params = { dateTime.created, dateTime.ended, store.getStoreId()};
                    QuantityOrdersDTO quantityOrdersDTO = ordersRepository.queryOrderUtilsForObject(sql,
                            QuantityOrdersDTO.class,params);
                    if (quantityOrdersDTO != null) {
                        NumberOrdersDTO numberOrdersDTO = new NumberOrdersDTO(store, quantityOrdersDTO);
                        listOrders.add(numberOrdersDTO);
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
        threadUtils.waitForFinish(executorService);
        List<NumberOrdersDTO> top20List = new ArrayList<>();

        // Lấy 20 phần tử đầu từ PriorityQueue và thêm vào danh sách mới
        while (top20List.size() < 20 && !listOrders.isEmpty()) {
            top20List.add(listOrders.poll());
        }
        excelUtils.createSheet(TOP_20_CUSTOMER+period);
        excelUtils.createHeaderRow(TOP_20_CUSTOMER+period, NumberOrdersDTO.class);
        int row = excelUtils.writeData(top20List);
        excelUtils.exportCustomExcel(response, TOP_20_CUSTOMER+period);

    }

    @Override
    public void exportRevenue(String created, String ended, HttpServletResponse response) throws IOException {
        DateTimeDTO dateTime = getDateTime(created, ended);
        String period = "("+created+"-"+ended+")";
        boolean fileExists = excelUtils.fileExist(REVENUE+period,response);
        if(fileExists) {
            return ;
        }
        String sql = """
                SELECT count(o.store_id) as quantityOrders,sum(total_item_price) totalPrice FROM fnb_orders.orders as o
                                        WHERE o.created_on between ? and ?

                                                    """;
        Object[] params = {dateTime.created, dateTime.ended};
        RevenueDTO revenueDTO = ordersRepository.queryOrderUtilsForObject(sql,
                RevenueDTO.class, params);
        if (revenueDTO != null) {
            RevenueExport revenueExport = new RevenueExport(revenueDTO);
            excelUtils.createSheet(REVENUE+period);
            excelUtils.createHeaderRow(REVENUE+period, RevenueExport.class);
            int row = excelUtils.writeSingelData(revenueExport);
            excelUtils.exportCustomExcel(response, REVENUE+period);
        }

    }

    public DateTimeDTO getDateTime(String dateCreated, String dateEnded) {
        Timestamp created = Timestamp.valueOf(dateCreated + " 00:00:00");
        Timestamp ended = Timestamp.valueOf(dateEnded + " 23:59:59");
        int createdAt = (int) (created.getTime() / 1000L); // chia cho 1000 để chuyển từ milliseconds sang seconds
        int endedAt = (int) (ended.getTime() / 1000L); // chia cho 1000 để chuyển từ milliseconds sang seconds
        return new DateTimeDTO(createdAt, endedAt);
    }
}
