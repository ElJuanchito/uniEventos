package co.edu.uniquindio.uni_eventos.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "order-details")
public class OrderDetail {

    @Id
    private String id;
    private ObjectId eventId;
    private Float price;
    private String sectionName;
    private Integer quantity;
}
