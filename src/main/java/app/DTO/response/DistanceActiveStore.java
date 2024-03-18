package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistanceActiveStore {
    private int less7;
    private int less10;
    private int less23;
    private int less30;
    private int more30;
}
