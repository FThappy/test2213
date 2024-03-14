package app.DTO.response;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountAHMGrabDTO  {
    private Integer storeId;
    private Number number;
    private String type;
}
