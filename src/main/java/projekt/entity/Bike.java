package projekt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bike {
    private int id;
    private String name;
    private BikeSize type;
    private BikeType size;
    private boolean available;
    private double pricePerDay;
    private String description;
    private String imageUrl;
}

