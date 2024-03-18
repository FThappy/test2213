package app.repository.timekeeping;

import app.DTO.response.QuantityTimeKeepDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TimeKeepRepositoryImp implements TimeKeepRepository{
    @Autowired
    @Qualifier("mariadbTimekeepingJdbcTemplate")
    private JdbcTemplate mariadbTimekeepingJdbcTemplate;

    @Override
    public List<QuantityTimeKeepDTO> queryListSuccessTimeKeep() {
        String sql = """
                    select store_id as storeid, count(*) as number
                    from fnb_timekeeping.attendances
                    where status = 'completed'
                    and note is null
                    group by store_id
                    order by store_id
                """;
        List<QuantityTimeKeepDTO> quantitySuccessTimeKeep = mariadbTimekeepingJdbcTemplate.query(sql, (rs, rownum) -> {
            return new QuantityTimeKeepDTO(rs.getInt("storeid"),
                    rs.getInt("number")
            );
        });
        return quantitySuccessTimeKeep;
    }

    @Override
    public List<QuantityTimeKeepDTO> queryListNotDetermined() {
        String sql = """
                    select store_id as storeid, count(*) as number
                    from fnb_timekeeping.attendances
                    where note = 'Không ghi nhận được vị trí'
                    group by store_id
                """;
        List<QuantityTimeKeepDTO> quantityNotDetermined = mariadbTimekeepingJdbcTemplate.query(sql, (rs, rownum) -> {
            return new QuantityTimeKeepDTO(rs.getInt("storeid"),
                    rs.getInt("number")
            );
        });
        return quantityNotDetermined;
    }

    @Override
    public List<QuantityTimeKeepDTO> queryListOutRange() {
        String sql = """
                            select store_id as storeid, count(*) as number
                            from fnb_timekeeping.attendances
                            where note = 'Không nằm trong phạm vi chấm công'
                            group by store_id
                        """;
        List<QuantityTimeKeepDTO> quantityOutRange = mariadbTimekeepingJdbcTemplate.query(sql, (rs, rownum) -> {
            return new QuantityTimeKeepDTO(rs.getInt("storeid"),
                    rs.getInt("number")
            );
        });
        return quantityOutRange;
    }

    @Override
    public List<QuantityTimeKeepDTO> queryListCases() {
        String sql = """
                            select store_id as storeid, count(*) as number
                            from fnb_timekeeping.shifts
                            group by store_id
                        """;
        List<QuantityTimeKeepDTO> quantityCases = mariadbTimekeepingJdbcTemplate.query(sql, (rs, rownum) -> {
            return new QuantityTimeKeepDTO(rs.getInt("storeid"),
                    rs.getInt("number")
            );
        });
        return quantityCases;
    }

    @Override
    public List<QuantityTimeKeepDTO> queryListMaxEmployees() {
        String sql = """
                            select store_id as storeid, max(number) as number
                            from (
                                select store_id, shift_id, count(*) as number
                                from fnb_timekeeping.shift_staff_mapping
                                group by store_id, shift_id
                            ) tmp
                            group by tmp.store_id
                        """;
        List<QuantityTimeKeepDTO> quantityMaxEmployee = mariadbTimekeepingJdbcTemplate.query(sql, (rs, rownum) -> {
            return new QuantityTimeKeepDTO(rs.getInt("storeid"),
                    rs.getInt("number")
            );
        });
        return quantityMaxEmployee;
    }

    @Override
    public List<QuantityTimeKeepDTO> queryListMinEmployees() {
        String sql = """
                            select store_id as storeid, min(number) as number
                            from (
                                select store_id, shift_id, count(*) as number
                                from fnb_timekeeping.shift_staff_mapping
                                group by store_id, shift_id
                            ) tmp
                            group by tmp.store_id
                        """;
        List<QuantityTimeKeepDTO> quantityMinEmployee = mariadbTimekeepingJdbcTemplate.query(sql, (rs, rownum) -> {
            return new QuantityTimeKeepDTO(rs.getInt("storeid"),
                    rs.getInt("number")
            );
        });
        return quantityMinEmployee;
    }
}
