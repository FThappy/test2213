package app.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DateTimeDTO {
    public int created;
    public int ended;
}
