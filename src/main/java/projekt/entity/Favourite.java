package projekt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favourite {
    private int id;
    private int userId;
    private int bikeId;
    private LocalDateTime addedAt;
}
