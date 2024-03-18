package app.DTO.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NumberStoreDTO {
    private int numberTrialStore;
    private int numberExpiredStore;
    private int numberValidStore;
}
