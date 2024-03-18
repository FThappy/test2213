package app.repository.customer;

import app.DTO.response.QuantityDebtDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepositoryImp implements CustomerRepository {
    @Autowired
    @Qualifier("mariadbCustomersJdbcTemplate")
    private JdbcTemplate mariadbCustomersJdbcTemplate;

    @Override
    public QuantityDebtDTO queryListCustomer(Object[] param) {
        String sql = """
                    select tmp.store_id as storeid, count(*) as number
                    from (select store_id, customer_id
                          from fnb_customers.customer_events
                          where store_id = ?
                            and type = 'debit'
                          group by store_id, customer_id) as tmp
                    group by tmp.store_id
                """;
        QuantityDebtDTO quantityCustomer = getQuantityDebtDTO(param, sql);
        return quantityCustomer;
    }

    @Override
    public QuantityDebtDTO queryListDebtCollection(Object[] param) {
        String sql = """
                    select store_id as storeid, count(*) as number
                    from fnb_customers.customer_events
                    where store_id = ?
                    and type = 'dcr_debt'
                    group by store_id;
                """;
        QuantityDebtDTO quantityListDebtCollection = getQuantityDebtDTO(param, sql);
        return quantityListDebtCollection;
    }

    @Override
    public QuantityDebtDTO queryListWriteDebt(Object[] param) {
        String sql = """
                    select store_id as storeid, count(*) as number
                    from fnb_customers.customer_events
                    where store_id = ?
                    and type = 'debit'
                    group by store_id;
                """;
        QuantityDebtDTO quantityListWriteDebt = getQuantityDebtDTO(param, sql);
        return quantityListWriteDebt;
    }

    private QuantityDebtDTO getQuantityDebtDTO(Object[] param, String sql) {
        QuantityDebtDTO quantityCustomer = mariadbCustomersJdbcTemplate.queryForObject(sql, (rs, rownum) -> {
            return new QuantityDebtDTO(rs.getInt("storeid"),
                    rs.getInt("number")
            );
        }, param);
        return quantityCustomer;
    }


    @Override
    public List<Integer> listMerchantId() {
        String sql = "select distinct merchant_id from fnb_customers.loyalty_cards";
        List<Integer> ids = mariadbCustomersJdbcTemplate.queryForList(sql, Integer.class);

        return ids;

    }
}
