package app.repository.store;

import app.DTO.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class StoreRepositoryImp implements StoreRepository{

    @Autowired
    @Qualifier("mariadbStoresJdbcTemplate")
    private JdbcTemplate mariadbStoresJdbcTemplate;
    @Override
    public List<StoreTimeKeepDTO> queryListStoreTimeKeep() {
        String sqlStoreId = """
                                select distinct s.server_id        as store_id,
                                s.name             as name,
                                sa.address         as address,
                                s.merchant_id      as merchantId,
                                s.store_package_id as storePackageId
                from fnb_stores.stores as s
                         left join fnb_stores.store_addresses sa on s.server_id = sa.store_id
                         left join fnb_timekeeping.timekeeping_qrcode as tq
                                   on s.server_id = tq.store_id
                                   where s.server_id not in (5000883,5000916)
                """;
        List<StoreTimeKeepDTO> listStoreQuery = mariadbStoresJdbcTemplate.query(sqlStoreId, (rs, rownum) -> {
            return new StoreTimeKeepDTO(rs.getInt("store_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getInt("merchantId"),
                    rs.getInt("storePackageId")
            );
        });
        return listStoreQuery;
    }

    @Override
    public List<StoreIdDTO> queryListStoreTaxItem() {
        String sqlStoreId = "select s.server_id        as store_id," +
                "       s.name             as name, " +
                "       sa.address         as address, " +
                "       s.phone            as phone, " +
                "       s.store_package_id as storePackageId " +
                " from fnb_stores.stores as s " +
                "         left join fnb_stores.store_addresses sa on s.server_id = sa.store_id " +
                "         left join taxes on s.server_id = taxes.store_id " +
                "where taxes.type = 'item'";
        List<StoreIdDTO> listTaxStoreId = getStoreIdDTOS(sqlStoreId);
        return listTaxStoreId;
    }

    @Override
    public List<StoreIdDTO> queryListStoreSpecialTime() {
        String sqlStoreId = "select distinct  s.server_id        as store_id, " +
                "       s.name             as name, " +
                "       sa.address         as address, " +
                "       s.phone            as phone, " +
                "       s.store_package_id as storePackageId " +
                "from fnb_stores.stores as s " +
                "         left join fnb_stores.store_addresses sa on s.server_id = sa.store_id " +
                "         left join fnb_products.item_variant_pricing_policies as ivpp " +
                " on s.server_id = ivpp.store_id " +
                "where ivpp.code = 'special'";
        List<StoreIdDTO> listStoreId = getStoreIdDTOS(sqlStoreId);
        return  listStoreId;
    }

    @Override
    public List<StoreIdDTO> queryListStoreDefaultItem() {
        String sqlStoreId = "select   s.server_id        as store_id , " +
                "       s.name             as name, " +
                "       sa.address         as address, " +
                "       s.phone            as phone, " +
                "       s.store_package_id as storePackageId " +
                "from fnb_stores.stores as s " +
                "         left join fnb_stores.store_addresses sa on s.server_id = sa.store_id " +
                "         left join fnb_products.default_items as di " +
                " on s.server_id = di.store_id ";
        List<StoreIdDTO> listStoreId = getStoreIdDTOS(sqlStoreId);
        return listStoreId;
    }

    @Override
    public List<StoreIdDTO> queryListStoreDebt() {
        String sqlStoreId = """
                        select distinct s.server_id as store_id,
                                        s.name as name,
                                        sa.address as address,
                                        s.phone as phone,
                                        s.store_package_id as storePackageId
                        from fnb_stores.stores as s
                             left join fnb_stores.store_addresses sa on s.server_id = sa.store_id
                             left join fnb_customers.customer_events as ce on s.server_id = ce.store_id
                        where ce.type = 'debit'
                    """;
        List<StoreIdDTO> listStoreId = getStoreIdDTOS(sqlStoreId);
        return listStoreId;
    }
@Override
    public List<StoreAccumulateDTO> queryStoreAccumulateDTOS(Integer params) {
        String sql = """
                    SELECT s.server_id AS storeId, s.name AS name,
                           sa.address AS address, sa.ward AS ward, sa.district AS district, sa.province AS province,
                           s.phone AS phone, s.store_package_id AS storePackageId, s.merchant_id AS merchantId
                    FROM stores AS s
                    LEFT JOIN store_addresses AS sa ON s.server_id = sa.store_id
                    WHERE s.merchant_id = ?
                """;
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
        }, params);
        return storeAccumulates;
    }
    private List<StoreIdDTO> getStoreIdDTOS(String sqlStoreId) {
        List<StoreIdDTO> listTaxStoreId = mariadbStoresJdbcTemplate.query(sqlStoreId, (rs, rownum) -> {
            return new StoreIdDTO(rs.getInt("store_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getInt("storePackageId")
            );
        });
        return listTaxStoreId;
    }

    @Override
    public List<Integer> queryListStoreIdMeInvoice() {
        String sql = "select store_id from e_invoice_partners where type = 'me_invoice';";
        List<Integer> listStoreId = getStoreId(sql);
        return listStoreId;
    }
    @Override
    public List<Integer> queryListStoreIdMInvoice() {
        String sql = "select store_id from e_invoice_partners where type = 'm_invoice';";
        List<Integer> listStoreId = getStoreId(sql);
        return listStoreId;
    }
    @Override
    public List<Integer> queryListStoreIdSInvoice() {
        String sql = "select store_id from e_invoice_partners where type = 's_invoice';";
        List<Integer> listStoreId = getStoreId(sql);
        return listStoreId;
    }

    @Override
    public List<Integer> queryListStoreAHM() {
        String sql = """
                            select store_id
                            from fnb_orders.shipping_methods
                            where type = 'ahamove'
                        """;
        List<Integer> listStoreId = getStoreId(sql);
        return listStoreId;
    }

    @Override
    public List<Integer> queryListStoreGrab() {
        String sql = """
                        select store_id
                        from fnb_orders.shipping_methods
                        where type = 'grabexpress'
                    """;
        List<Integer> listStoreId = getStoreId(sql);
        return listStoreId;
    }

    @Override
    public List<Integer> queryListStoreMPOS() {
        String sqlGetStoreId = "select store_id from fnb_orders.payment_methods where type = 'mPOS';";
        List<Integer> listStoreId = getStoreId(sqlGetStoreId);
        return listStoreId;
    }

    @Override
    public List<Integer> queryListStoreOCB() {
        String sqlGetStoreId = "select store_id from fnb_orders.payment_methods where type = 'ocb';";
        List<Integer> listStoreId = getStoreId(sqlGetStoreId);
        return listStoreId;
    }

    @Override
    public List<Integer> queryListStoreTCB() {
        String sqlGetStoreId = "select store_id from fnb_orders.payment_methods where type = 'tcb';";
        List<Integer> listStoreId = getStoreId(sqlGetStoreId);
        return listStoreId;
    }

    @Override
    public List<Integer> queryListActiveStore() {
        String sqlStoreId = """
                    SELECT s.server_id
                    from fnb_stores.stores as s
                             LEFT JOIN fnb_stores.store_packages sp on s.store_package_id = sp.server_id
                    WHERE s.end_date > ?
                      AND sp.package_name <> 'FnB Trial'
                    """;
        List<Integer> listStoreId = getStoreId(sqlStoreId);
        return listStoreId;
    }

    @Override
    public List<Integer> queryListTrialStore() {
        String sql = """
                        SELECT s.server_id
                        from fnb_stores.stores as s
                                 LEFT JOIN fnb_stores.store_packages sp on s.store_package_id = sp.server_id
                        WHERE sp.package_name = 'FnB Trial';
                        """;
        List<Integer> listStoreId = getStoreId(sql);
        return listStoreId;

    }

    private List<Integer> getStoreId(String sql) {
            List<Integer> listStoreId = mariadbStoresJdbcTemplate.queryForList(sql, Integer.class);
            return listStoreId;
    }
    @Override
    public List<DataStoreConnectWebOrderDTO> listStoreConnectWeborder() {
        String sql = """
                        select distinct (s.server_id) as storeid,
                                        s.name as name, 
                                        s.phone as phone,
                                        sa.address as address,
                                        sa.ward as ward,
                                        sa.district as district,
                                        sa.province as province,
                                        m.alias as alias,
                                        from_unixtime(ml.created_on) as createdon,
                                        s.store_package_id as storePackageId 
                        from stores as s, store_addresses as sa, merchants as m, store_settings as ss,
                             merchant_logs as ml
                        where sa.store_id = s.server_id
                        and s.merchant_id = m.server_id
                        and ss.store_id = s.server_id
                        and ss.setting_key = 'web_order'
                        and ml.merchant_id = s.merchant_id
                        and m.server_id != 11446
                        and m.init_website = true
                        group by s.server_id
                    """;
        List<DataStoreConnectWebOrderDTO> listStoreConnectWebOrder = mariadbStoresJdbcTemplate.query(sql,
                (rs, rownum) -> {
                    return new DataStoreConnectWebOrderDTO(
                            rs.getInt("storeid"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("ward"),
                            rs.getString("district"),
                            rs.getString("province"),
                            rs.getString("alias"),
                            rs.getTimestamp("createdon"),
                            rs.getInt("storePackageId")
                    );
                });
        return listStoreConnectWebOrder;
    }

    @Override
    public List<StoreOrdersDTO> listStoreIdCustomer() {
        String sqlStoreId = """
                 SELECT s.server_id as storeid,s.name as name, sa.address as address , s.phone as phone FROM fnb_stores.stores s
                LEFT JOIN fnb_stores.store_addresses sa on s.server_id = sa.store_id;
                                    """;
        List<StoreOrdersDTO> listStoreId = mariadbStoresJdbcTemplate.query(sqlStoreId, (rs, rownum) -> {
            return new StoreOrdersDTO(rs.getInt("storeid"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone")
            );
        });
        return listStoreId;
    }

    @Override
    public List<Integer> queryListValidStore(Object[] params) {
        String sql = """
                        SELECT s.server_id
                        from fnb_stores.stores as s
                                 LEFT JOIN fnb_stores.store_packages sp on s.store_package_id = sp.server_id
                        WHERE s.end_date > ?
                          AND sp.package_name <> 'FnB Trial';
                                                """;
        List<Integer> listStoreId = getStoreId(sql,params);
        return listStoreId;
    }

    @Override
    public List<Integer> queryListExpiredStore(Object[] params) {
        String sql = """
                         SELECT s.server_id
                        from fnb_stores.stores as s
                                 LEFT JOIN fnb_stores.store_packages sp on s.store_package_id = sp.server_id
                        WHERE s.end_date < ?
                          AND sp.package_name <> 'FnB Trial';
                                                 """;
        List<Integer> listStoreId = getStoreId(sql,params);
        return listStoreId;
    }
    private List<Integer> getStoreId(String sql,Object[] params) {
        List<Integer> listStoreId = mariadbStoresJdbcTemplate.queryForList(sql, Integer.class);
        return listStoreId;
    }
}
