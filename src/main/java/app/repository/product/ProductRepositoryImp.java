package app.repository.product;

import app.DTO.response.QuantitySpecialTime;
import app.DTO.response.QuantityTaxDTO;
import app.DTO.response.QuantiyDefaultItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImp implements ProductRepository {
    @Autowired
    @Qualifier("mariadbProductsJdbcTemplate")
    private JdbcTemplate mariadbProductsJdbcTemplate;

    @Override
    public QuantityTaxDTO queryQuantityTaxDTO(Object[] parameter) {
        String sql = "select store_id as storeid, count(*) as number from fnb_products.items " +
                " where store_id = ?  " +
                " and tax is not null " +
                " and tax > 0 " +
                " group by store_id ";
        QuantityTaxDTO quantityTaxDTO = mariadbProductsJdbcTemplate.queryForObject(sql, (rs, rownum) -> {
            return new QuantityTaxDTO(rs.getInt("storeid"),
                    rs.getInt("number")
            );
        }, parameter);
        return quantityTaxDTO;
    }

    @Override
    public QuantitySpecialTime queryQuantitySpecialTime(Object[] parameter) {
        String sql = "select store_id as storeid, count(*)as number from fnb_products.item_variant_pricing_policies " +
                " where store_id = ? " +
                " and code = 'special' " +
                " group by store_id ";
        QuantitySpecialTime quantitySpecialTime = mariadbProductsJdbcTemplate.queryForObject(sql, (rs, rownum) -> {
            return new QuantitySpecialTime(rs.getInt("storeid"),
                    rs.getInt("number")
            );
        }, parameter);
        return quantitySpecialTime;
    }

    @Override
    public QuantiyDefaultItem queryQuantityDefaultItem(Object[] parameter) {
        String sql = """
                    select store_id as storeid, count(*) as number
                    from fnb_products.default_item_mappings
                    where store_id = ?
                    group by store_id
                """;
        QuantiyDefaultItem quantiyDefaultItem = mariadbProductsJdbcTemplate.queryForObject(sql, (rs, rownum) -> {
            return new QuantiyDefaultItem(rs.getInt("storeid"),
                    rs.getInt("number")
            );
        }, parameter);
        return quantiyDefaultItem;
    }
}
