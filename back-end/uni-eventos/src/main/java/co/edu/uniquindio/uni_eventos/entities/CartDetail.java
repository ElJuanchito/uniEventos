package co.edu.uniquindio.uni_eventos.entities;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class CartDetail {

    private Integer quantity;
    private String sectionName;
    private ObjectId eventId;
}
