package app.repository.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersRepositoryImp implements OrdersRepository {

    @Autowired
    @Qualifier("mariadbOrdersJdbcTemplate")
    private JdbcTemplate mariadbOrdersJdbcTemplate;

    @Autowired
    @Qualifier("mariadbOrders2JdbcTemplate")
    private JdbcTemplate mariadbOrders2JdbcTemplate;

    @Override
    public <T> T queryOrderUtilsForSingleData(String sql, Class<T> clazz, Object[] parameter) {
        int[] argTypes = new int[]{java.sql.Types.INTEGER};
        try {
            T result = mariadbOrdersJdbcTemplate.queryForObject(sql,parameter,argTypes, clazz );
            System.out.println(parameter);
            if (result != null) {
                System.out.println(result);
                return result;
            } else {
                result = mariadbOrders2JdbcTemplate.queryForObject(sql, parameter,argTypes ,clazz);
                return result;
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ ở đây, ví dụ: in ra thông báo lỗi và trả về giá trị mặc định
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public <T> T queryOrderUtilsForObject(String sql, Class<T> clazz, Object[] parameter) {
        try {
            return queryOrders(sql, clazz, parameter);
        } catch (Exception e) {

            return null;
        }
    }
    private <T> T queryOrders(String sql, Class<T> clazz, Object[] parameter) {
        T result = mariadbOrdersJdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(clazz),parameter);
        if (result != null) {
            return result;
        } else {
            result = mariadbOrders2JdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(clazz),parameter);
            return result;
        }
    }
}
