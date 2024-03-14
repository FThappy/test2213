package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountTimeKeepDTO {
    private int storeId;
    private Number number;
}
