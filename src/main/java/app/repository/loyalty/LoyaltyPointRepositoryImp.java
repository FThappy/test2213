package app.repository.loyalty;

import app.DTO.response.QuantityAccmulationUsedDTO;
import app.DTO.response.QuantityAccumulationFormDTO;
import app.DTO.response.StoreAccumulationUsedResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoyaltyPointRepositoryImp implements LoyaltyPointRepository {

    @Autowired
    @Qualifier("mariadbOrdersJdbcTemplate")
    private JdbcTemplate mariadbOrdersJdbcTemplate;

    @Override
    public QuantityAccmulationUsedDTO queryListStoreAccmulationUsed(Object[] params) {
        String sql = """
                    select store_id as storeid, count(*) as quantity 
                    from order_discounts 
                    where store_id = ? and discount_type = 'loyalty_card' 
                    group by store_id;
                """;
        QuantityAccmulationUsedDTO countAccumulationUsedDTO = mariadbOrdersJdbcTemplate.queryForObject(sql, (rs, rownum) -> {
            return new QuantityAccmulationUsedDTO(rs.getInt("storeid"),
                    rs.getInt("quantity")
            );
        }, params);
        return countAccumulationUsedDTO;
    }

    @Override
    public QuantityAccumulationFormDTO quantityAccumulationFormDTO(Object[] params) {
        String sql = """
                    select olc.store_id as storeid, count(*) as quantity
                    from order_loyalty_cards olc
                    where olc.store_id = ?
                    group by olc.store_id;
                """;
        QuantityAccumulationFormDTO quantityAccumulationFormDTO = mariadbOrdersJdbcTemplate.queryForObject(sql, (rs, rownum) -> new QuantityAccumulationFormDTO(rs.getInt("storeid"),
                rs.getInt("quantity")
        ), params);
        return quantityAccumulationFormDTO;
    }
}
